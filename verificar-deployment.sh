#!/bin/bash
# Script de verificação para deployment do Animalys no Linux
# Execute este script antes de fazer o deploy para verificar se tudo está configurado corretamente

echo "========================================="
echo "Verificação de Deployment - Animalys"
echo "========================================="
echo ""

# Cores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Função para verificar
check() {
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓${NC} $1"
    else
        echo -e "${RED}✗${NC} $1"
    fi
}

# 1. Verificar Java
echo "1. Verificando Java..."
java -version 2>&1 | grep "version" > /dev/null
check "Java instalado"
java -version 2>&1

echo ""

# 2. Verificar Tomcat
echo "2. Verificando Tomcat..."
if [ -z "$CATALINA_HOME" ]; then
    echo -e "${YELLOW}⚠${NC} CATALINA_HOME não está definido"
    echo "   Tentando localizar Tomcat..."
    
    # Procurar em locais comuns
    TOMCAT_LOCATIONS=(
        "/opt/tomcat"
        "/usr/share/tomcat"
        "/usr/share/tomcat9"
        "/var/lib/tomcat9"
        "/usr/local/tomcat"
    )
    
    for location in "${TOMCAT_LOCATIONS[@]}"; do
        if [ -d "$location" ]; then
            echo -e "${GREEN}✓${NC} Tomcat encontrado em: $location"
            export CATALINA_HOME=$location
            break
        fi
    done
else
    echo -e "${GREEN}✓${NC} CATALINA_HOME definido: $CATALINA_HOME"
fi

echo ""

# 3. Verificar diretórios do Animalys
echo "3. Verificando diretórios do Animalys..."
ANIMALYS_BASE="$HOME/animalys"

if [ -d "$ANIMALYS_BASE" ]; then
    echo -e "${GREEN}✓${NC} Diretório base existe: $ANIMALYS_BASE"
else
    echo -e "${YELLOW}⚠${NC} Diretório base não existe: $ANIMALYS_BASE"
    echo "   Criando diretórios..."
    mkdir -p "$ANIMALYS_BASE"/{documentos,fotos,receitas}
    check "Diretórios criados"
fi

# Verificar subdiretórios
for dir in documentos fotos receitas; do
    if [ -d "$ANIMALYS_BASE/$dir" ]; then
        echo -e "${GREEN}✓${NC} $ANIMALYS_BASE/$dir existe"
    else
        echo -e "${RED}✗${NC} $ANIMALYS_BASE/$dir não existe"
        mkdir -p "$ANIMALYS_BASE/$dir"
        check "Criado $ANIMALYS_BASE/$dir"
    fi
done

echo ""

# 4. Verificar permissões
echo "4. Verificando permissões..."
for dir in documentos fotos receitas; do
    if [ -w "$ANIMALYS_BASE/$dir" ]; then
        echo -e "${GREEN}✓${NC} $ANIMALYS_BASE/$dir tem permissão de escrita"
    else
        echo -e "${RED}✗${NC} $ANIMALYS_BASE/$dir SEM permissão de escrita"
        chmod 755 "$ANIMALYS_BASE/$dir"
        check "Permissões ajustadas para $dir"
    fi
done

echo ""

# 5. Verificar espaço em disco
echo "5. Verificando espaço em disco..."
AVAILABLE_SPACE=$(df -h "$HOME" | awk 'NR==2 {print $4}')
echo "   Espaço disponível em $HOME: $AVAILABLE_SPACE"

# Converter para MB para comparação
AVAILABLE_MB=$(df -m "$HOME" | awk 'NR==2 {print $4}')
if [ $AVAILABLE_MB -gt 1000 ]; then
    echo -e "${GREEN}✓${NC} Espaço em disco suficiente"
else
    echo -e "${YELLOW}⚠${NC} Pouco espaço em disco disponível"
fi

echo ""

# 6. Verificar usuário do Tomcat
echo "6. Verificando usuário do Tomcat..."
if pgrep -u root tomcat > /dev/null; then
    echo -e "${YELLOW}⚠${NC} Tomcat rodando como root"
elif pgrep tomcat > /dev/null; then
    TOMCAT_USER=$(ps aux | grep tomcat | grep -v grep | awk '{print $1}' | head -1)
    echo -e "${GREEN}✓${NC} Tomcat rodando como usuário: $TOMCAT_USER"
    
    if [ "$TOMCAT_USER" != "$USER" ]; then
        echo -e "${YELLOW}⚠${NC} Tomcat roda como '$TOMCAT_USER', mas você é '$USER'"
        echo "   Você pode precisar ajustar as permissões:"
        echo "   sudo chown -R $TOMCAT_USER:$TOMCAT_USER $ANIMALYS_BASE"
    fi
else
    echo -e "${YELLOW}⚠${NC} Tomcat não está rodando"
fi

echo ""

# 7. Verificar arquivo WAR
echo "7. Verificando arquivo WAR..."
if [ -f "target/animalys-1.0.0-SNAPSHOT.war" ]; then
    echo -e "${GREEN}✓${NC} Arquivo WAR encontrado"
    WAR_SIZE=$(du -h target/animalys-1.0.0-SNAPSHOT.war | awk '{print $1}')
    echo "   Tamanho: $WAR_SIZE"
else
    echo -e "${YELLOW}⚠${NC} Arquivo WAR não encontrado"
    echo "   Execute: mvn clean package"
fi

echo ""

# 8. Resumo
echo "========================================="
echo "RESUMO"
echo "========================================="
echo ""
echo "Diretórios do Animalys:"
echo "  Base: $ANIMALYS_BASE"
echo "  Documentos: $ANIMALYS_BASE/documentos"
echo "  Fotos: $ANIMALYS_BASE/fotos"
echo "  Receitas: $ANIMALYS_BASE/receitas"
echo ""

if [ ! -z "$CATALINA_HOME" ]; then
    echo "Tomcat:"
    echo "  CATALINA_HOME: $CATALINA_HOME"
    echo "  Deploy: cp target/*.war $CATALINA_HOME/webapps/"
    echo ""
fi

echo "Próximos passos:"
echo "  1. Compile o projeto: mvn clean package"
echo "  2. Faça o deploy: cp target/*.war $CATALINA_HOME/webapps/"
echo "  3. Reinicie o Tomcat: sudo systemctl restart tomcat9"
echo "  4. Verifique os logs: tail -f $CATALINA_HOME/logs/catalina.out"
echo ""
echo "========================================="
