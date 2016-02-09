/// <reference path="jquery1.9.1.min.js" />
/// <reference path="jquery1.9.1.min.js" />
$(function () {
    init();
})
function init() {
    initBind();
}
function initBind() {
    $(".mask .img-box").on("click", function () {
        var box = $(".mask .img-box");
        var mask = $(".mask");
        box.animate({ "margin-top": -120, opacity: 0 }, 400, function () {
            mask.fadeOut(300);
        });
    });
}
function bind() {
    $(document).on("click","a[ref]", function () {
        var box = $(".mask .img-box");
        var mask = $(".mask");
        var ref = $(this).attr("ref");
        var top = $(window).height()/2-125;
        mask.fadeIn(300, function () {
            box.css("background-image","url("+ref+")").animate({ "margin-top": top, opacity: 1 }, 400);
        })
    });
}