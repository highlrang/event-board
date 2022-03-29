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
                alert(`저장 성공 id = ${result}`);
                // location.href=`/board/detail/${result}`;
            },
            error: function(error){
                let errRes = JSON.parse(error.responseText);
                alert(errRes.body[0]);
                // $.each(errRes.body, function(idx, msg){
                //     alert(msg);
                // });
            }

        });
    },


}