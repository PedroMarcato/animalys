$(function(){

	var selecao = $('.js-check');
	var btCadastro = $('.js-bt-cadastro');


	btCadastro.on('click', function(){		
		
		if(selecao.is(':checked')){
			window.location.replace("http://ti.guaira.pr.gov.br/animalys/cadastros/cadastroAnimal.xhtml");
		}else{
			alert('Se está de acordo, marque a opção "Li e concordo com os critérios para submeter o meu animal ao programa de castração gratuita" antes de continuar!');
		}

	});

});


// Toggle Function
$('.toggle').click(function(){
  // Switches the Icon
  $(this).children('i').toggleClass('fa-pencil');
  // Switches the forms  
  $('.form').animate({
    height: "toggle",
    'padding-top': 'toggle',
    'padding-bottom': 'toggle',
    opacity: "toggle"
  }, "slow");
});

