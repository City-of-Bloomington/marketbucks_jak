clicked_button_id="";
var icons = {
    header:"ui-icon-circle-plus",
    activeHeader:"ui-icon-circle-minus"
};

$(".date").datepicker({
    nextText: "Next",
    prevText:"Prev",
    buttonText: "Pick Date",
    showOn: "both",
    navigationAsDateFormat: true,
    buttonImage: "/marketbucks/js/calendar.gif"
});

$("#vendorName").autocomplete({
    source: APPLICATION_URL + "VendorService?format=json",
    minLength: 2,
    delay: 100,
    select: function( event, ui ) {
        if(ui.item){
            $("#vendor_id").val(ui.item.id);
            $("#action_id").val('Next');
            $("#form_id").submit();
        }
    }
});


$(function() {
    $("#bar_code_id").focus();
    $("#vendor_id").focus();
    $(".need_focus").focus();
});

$("#div3").change(function() {
    var xx = $("#div3").val();
    if( xx % 3 > 0){
        alert("The amount is not divisible by 3");
        $("#div3").focus();
    }
});


$("#div5").change(function() {
    var xx = $("#div5").val();
    if( xx % 5 > 0){
        alert("The amount is not divisible by 5");
        $("#div5").focus();
    }
});

jQuery(function ($) {
    var launcherClick = function(e)  {
            var openMenus   = $('.menuLinks.open'),
                menu        = $(e.target).siblings('.menuLinks');
            openMenus.removeClass('open');
            setTimeout(function() { openMenus.addClass('closed'); }, 300);

            menu.removeClass('closed');
            menu.   addClass('open');
            e.stopPropagation();
        },
        documentClick = function(e) {
            var openMenus   = $('.menuLinks.open');

            openMenus.removeClass('open');
            setTimeout(function() { openMenus.addClass('closed'); }, 300);
        };
    $('.menuLauncher').click(launcherClick);
    $(document       ).click(documentClick);
});
$(document).on("click","button", function (event) {
	clicked_button_id = event.target.id;
});
function confirmForCancel(){
	if(clicked_button_id == 'cancel_button'){
		clicked_button_id = "";		
		return confirm("Are you sure you want to cancel the transaction");
	}
	return true;
}
function setGl_account(){
		var face_values = ["","3","5","20"];
		var max_donates = ["","18","",""];
		var jj = $("#type_id option:selected").index();
		if(jj != "0"){
				$("#gl_account_id option")[jj].selected=true;
				$("#face_value_id").val(face_values[jj]);
				$("#max_don_id").val(max_donates[jj]);				
		}
		else if(jj == "0"){
				$("#gl_account_id").val("-1");
				$("#face_value_id").val("");
				$("#max_don_id").val("");				
		}
}

