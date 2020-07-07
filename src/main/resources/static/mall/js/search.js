$(function () {
    $('#keyword').keypress(function (e) {
        var key = e.which; //e.which是按键的值
        if (key == 13) {
            var q = $(this).val();
            if (q && q != '') {
                window.location.href = '/search?keyword=' + q;
            }
        }
    });
});

function search() {
    var q = $('#keyword').val();
    if (q && q != '') {
        window.location.href = '/search?keyword=' + q;
    }
}

function imgPreview(fileDom, i) {
    //判断是否支持FileReader
    if (window.FileReader) {
        var reader = new FileReader();
    } else {
        alert("您的设备不支持图片预览功能，如需该功能请升级您的设备！");
    }
    //获取文件
    var file = fileDom.files[0];
    var imageType = /^image\//;
    //是否是图片
    if (!imageType.test(file.type)) {
        alert("请选择图片！");
        return;
    }
    //读取完成
    reader.onload = function (e) {
        //图片路径设置为读取的图片
        // img.src = e.target.result;
        console.log(document.getElementsByClassName('icon-picture-search'));
        document.getElementsByClassName('icon-picture-search')[i].style.background = "url(" + e.target.result + ")no-repeat";//回显图片
        document.getElementsByClassName('icon-picture-search')[i].style.backgroundSize = '200px 160px';
        console.log('reader', reader)
    };
    reader.readAsDataURL(file);
}
