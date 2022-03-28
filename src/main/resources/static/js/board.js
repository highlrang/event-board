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
            success: function(result){
                alert(`저장 성공 id = ${result}`);
                // location.href=`/board/detail/${result}`;
            },
            error: function(error){
                console.log();
                alert("게시글이 저장되지 않았습니다.");
            }

        });
    },


}