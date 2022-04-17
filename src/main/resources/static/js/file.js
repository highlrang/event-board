const file = {
    save: function(){
        const form = new FormData($("form")[0]);

        $.ajax({
                type: "POST",
                url: "/api/file",
                data: form,
                enctype: "multipart/form-data",
                contentType: false,
                processData: false,
                dataType: "json",
                success: function(result){
                    $("#fileId").val(result.id);
                    $("#boardImage").attr('alt', result.name);
                    $("#boardImage").css('display', 'block');
                },
                error: function(error){
                    let msg = "파일 업로드에 실패하였습니다.";
                    if(error.status === 400) msg = "이미지만 업로드가 가능합니다.";
                    alert(msg);
                    $("input[type=file]").val("");
                    $("#boardImage").css('display', 'none');
                }
        });
    },

    delete: function(id){
        callAjax("DELETE", `/api/file/${id}`, null, () => {
            $("#boardImage").css('display', 'none');
            $("input[type=file]").val("");
            $("#fileId").val("");
        });
    }
}