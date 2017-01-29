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