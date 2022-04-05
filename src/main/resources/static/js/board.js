const board = {

    save: function () {
        const form = new FormData($("form")[0]);

        $.ajax({
            type: "POST",
            url: "/api/board",
            data: form,
            enctype: "multipart/form-data",
            contentType: false,
            processData: false,
            dataType: "json",
            success: function(result){
                alert("게시글이 저장되었습니다.");
                location.href=`/board/${result}`;
            },
            error: function(error){
                let errRes = JSON.parse(error.responseText);
                Array.isArray(errRes.body) && alert(errRes.body[0]);
            }

        });
    },

    downloadFile: function(id){
        callAjax("GET", `/api/board/file-download/${id}`, null, null);
    }


}