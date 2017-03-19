jQuery(document).ready(function($) {
    $(".clickable-row").click(function() {
        window.document.location = $(this).data("href");
    });
});

$(document).on("click", ".open-deleteArticleDialog", function () {
    var myArticleId = $(this).data('id');
    $("#articleId").val( myArticleId );
    // As pointed out in comments,
    // it is superfluous to have to manually call the modal.
    // $('#addBookDialog').modal('show');
});

$(document).on("change", ".delivery", function () {
    if($(this).val() != "No"){
        $('#address').removeClass('invisible');
        $('#address').addClass('address-div');
        $("#addressLine").prop('required',true);
    } else {
        $('#address').addClass('invisible');
        $('#address').removeClass('address-div');
        $("#addressLine").prop('required',false);
    }
});


$(document).on("change", "#select-all", function () {
        var checked = this.checked;
        $('.check-box').each(function () {
            this.checked = checked;
        });
});