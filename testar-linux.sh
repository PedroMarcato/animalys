#!/bin/bash
# Script de teste automatizado para Linux
# Testa se o deploy funcionou corretamente

echo "========================================="
echo "  TESTE AUTOMATIZADO - ANIMALYS (LINUX)"
echo "========================================="
echo ""

# Cores
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Contadores
PASSED=0
FAILED=0

# Função para verificar
check_pass() {
    echo -e "${GREEN}[PASS]${NC} $1"
    ((PASSED++))
}

check_fail() {
    echo -e "${RED}[FAIL]${NC} $1"
    ((FAILED++))
}

check_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

check_info() {
    echo -e "${YELLOW}[INFO]${NC} $1"
}

# 1. Verificar Java
echo "1. VERIFICANDO JAVA..."
echo "-----------------------------------------"
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1)
    check_pass "Java instalado: $JAVA_VERSION"
else
    check_fail "Java não encontrado"
fi
echo ""

# 2. Verificar Tomcat
echo "2. VERIFICANDO TOMCAT..."
echo "-----------------------------------------"
if pgrep -x "java" > /dev/null && pgrep -f "tomcat" > /dev/null; then
    TOMCAT_USER=$(ps aux | grep tomcat | grep -v grep | awk '{print $1}' | head -1)
    check_pass "Tomcat rodando como usuário: $TOMCAT_USER"
elif systemctl is-active --quiet tomcat9; then
    check_pass "Tomcat9 está ativo (systemd)"
else
    check_warn "Tomcat não está rodando"
fi
echo ""

# 3. Verificar diretórios do Animalys
echo "3. VERIFICANDO DIRETORIOS DO ANIMALYS..."
echo "-----------------------------------------"
ANIMALYS_BASE="$HOME/animalys"

if [ -d "$ANIMALYS_BASE" ]; then
    check_pass "Diretório base existe: $ANIMALYS_BASE"
else
    check_fail "Diretório base não existe: $ANIMALYS_BASE"
fi

for dir in documentos fotos receitas; do
    if [ -d "$ANIMALYS_BASE/$dir" ]; then
        check_pass "Diretório $dir existe"
    else
        check_fail "Diretório $dir NÃO existe"
    fi
done
echo ""

# 4. Verificar permissões
echo "4. VERIFICANDO PERMISSOES..."
echo "-----------------------------------------"
for dir in documentos fotos receitas; do
    if [ -w "$ANIMALYS_BASE/$dir" ]; then
        check_pass "$dir tem permissão de escrita"
    else
        check_fail "$dir SEM permissão de escrita"
    fi
done

# Testar criação de arquivo
touch "$ANIMALYS_BASE/documentos/.test" 2>/dev/null
if [ -f "$ANIMALYS_BASE/documentos/.test" ]; then
    check_pass "Consegue criar arquivos"
    rm "$ANIMALYS_BASE/documentos/.test"
else
    check_fail "Não consegue criar arquivos"
fi
echo ""

# 5. Verificar espaço em disco
echo "5. VERIFICANDO ESPACO EM DISCO..."
echo "-----------------------------------------"
AVAILABLE_MB=$(df -m "$HOME" | awk 'NR==2 {print $4}')
AVAILABLE=$(df -h "$HOME" | awk 'NR==2 {print $4}')
echo "   Espaço disponível: $AVAILABLE"

if [ $AVAILABLE_MB -gt 1000 ]; then
    check_pass "Espaço em disco suficiente (> 1GB)"
else
    check_warn "Pouco espaço em disco ($AVAILABLE)"
fi
echo ""

# 6. Verificar deploy da aplicação
echo "6. VERIFICANDO DEPLOY DA APLICACAO..."
echo "-----------------------------------------"

# Procurar em locais comuns do Tomcat
TOMCAT_WEBAPPS_LOCATIONS=(
    "/var/lib/tomcat9/webapps"
    "/var/lib/tomcat/webapps"
    "/opt/tomcat/webapps"
    "/usr/share/tomcat9/webapps"
    "$CATALINA_HOME/webapps"
)

FOUND_WEBAPP=false
for location in "${TOMCAT_WEBAPPS_LOCATIONS[@]}"; do
    if [ -d "$location/animalys" ]; then
        check_pass "Aplicação deployada em: $location/animalys"
        WEBAPP_DIR="$location/animalys"
        FOUND_WEBAPP=true
        break
    fi
done

if ! $FOUND_WEBAPP; then
    check_warn "Aplicação não encontrada nos locais padrão"
fi
echo ""

# 7. Verificar web.xml deployado
echo "7. VERIFICANDO WEB.XML DEPLOYADO..."
echo "-----------------------------------------"
if [ -n "$WEBAPP_DIR" ] && [ -f "$WEBAPP_DIR/WEB-INF/web.xml" ]; then
    if grep -q "user.home" "$WEBAPP_DIR/WEB-INF/web.xml"; then
        check_pass "web.xml contém \${user.home}"
    else
        check_warn "web.xml NÃO contém \${user.home} (pode precisar ajuste manual)"
    fi
else
    check_warn "web.xml não encontrado"
fi
echo ""

# 8. Verificar classes compiladas
echo "8. VERIFICANDO CLASSES COMPILADAS..."
echo "-----------------------------------------"
if [ -n "$WEBAPP_DIR" ]; then
    CLASSES_DIR="$WEBAPP_DIR/WEB-INF/classes"
    
    if [ -f "$CLASSES_DIR/br/gov/pr/guaira/animalys/util/FileUploadUtil.class" ]; then
        check_pass "FileUploadUtil.class encontrada"
    else
        check_fail "FileUploadUtil.class NÃO encontrada"
    fi
    
    if [ -f "$CLASSES_DIR/br/gov/pr/guaira/animalys/servlet/FotoServlet.class" ]; then
        check_pass "FotoServlet.class encontrada"
    else
        check_fail "FotoServlet.class NÃO encontrada"
    fi
    
    if [ -f "$CLASSES_DIR/br/gov/pr/guaira/animalys/service/FotoService.class" ]; then
        check_pass "FotoService.class encontrada"
    else
        check_fail "FotoService.class NÃO encontrada"
    fi
fi
echo ""

# 9. Verificar logs do Tomcat
echo "9. VERIFICANDO LOGS DO TOMCAT..."
echo "-----------------------------------------"

TOMCAT_LOG_LOCATIONS=(
    "/var/log/tomcat9/catalina.out"
    "/var/log/tomcat/catalina.out"
    "/opt/tomcat/logs/catalina.out"
    "$CATALINA_HOME/logs/catalina.out"
)

FOUND_LOG=false
for log in "${TOMCAT_LOG_LOCATIONS[@]}"; do
    if [ -f "$log" ]; then
        check_pass "Log encontrado: $log"
        
        # Verificar mensagens importantes
        if grep -q "Diretório de fotos inicializado" "$log"; then
            check_pass "FotoService inicializado corretamente"
            FOTO_DIR=$(grep "Diretório de fotos inicializado" "$log" | tail -1 | cut -d: -f3-)
            echo "        Diretório: $FOTO_DIR"
        else
            check_warn "Mensagem de inicialização de fotos não encontrada"
        fi
        
        # Procurar por erros
        if tail -100 "$log" | grep -i "error" | grep -i "animalys" > /dev/null; then
            check_warn "Erros encontrados nos logs (verifique manualmente)"
        fi
        
        FOUND_LOG=true
        break
    fi
done

if ! $FOUND_LOG; then
    check_warn "Log do Tomcat não encontrado"
fi
echo ""

# 10. Teste de conectividade (se Tomcat estiver rodando)
echo "10. TESTANDO CONECTIVIDADE..."
echo "-----------------------------------------"
if command -v curl &> /dev/null; then
    if curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/animalys/ | grep -E "200|302" > /dev/null; then
        check_pass "Aplicação respondendo em http://localhost:8080/animalys/"
    else
        check_warn "Aplicação não responde (Tomcat rodando?)"
    fi
else
    check_info "curl não instalado, pulando teste de conectividade"
fi
echo ""

# Resumo
echo "========================================="
echo "  RESULTADO DOS TESTES"
echo "========================================="
echo ""
echo "Total de testes executados: 10"
echo -e "${GREEN}Testes aprovados:${NC} $PASSED"
if [ $FAILED -gt 0 ]; then
    echo -e "${RED}Testes falhados:${NC} $FAILED"
else
    echo "Testes falhados: $FAILED"
fi
echo ""

if [ $FAILED -eq 0 ]; then
    echo -e "${GREEN}=========================================${NC}"
    echo -e "${GREEN}  TODOS OS TESTES PASSARAM!${NC}"
    echo -e "${GREEN}  Aplicação pronta para uso!${NC}"
    echo -e "${GREEN}=========================================${NC}"
    echo ""
    echo "Próximos passos:"
    echo "  1. Acesse: http://seu-servidor:8080/animalys"
    echo "  2. Faça login"
    echo "  3. Teste uploads de arquivos"
    echo "  4. Verifique: ls -la ~/animalys/fotos/"
    exit 0
else
    echo -e "${RED}=========================================${NC}"
    echo -e "${RED}  ALGUNS TESTES FALHARAM!${NC}"
    echo -e "${RED}  Verifique os problemas acima${NC}"
    echo -e "${RED}=========================================${NC}"
    echo ""
    echo "Comandos úteis:"
    echo "  Ver logs: tail -f /var/log/tomcat9/catalina.out"
    echo "  Criar diretórios: mkdir -p ~/animalys/{documentos,fotos,receitas}"
    echo "  Ajustar permissões: chmod 755 ~/animalys -R"
    exit 1
fi
