const board = {

    save: function () {
        const data = {
            "boardType": $("#boardType").val(),
            "writerId": 1, // security에서 getUserId,
            "title" : $("#title").val(),
            "content" : $("#content").val(),
            "startDate": $("#startDate").val(),
            "endDate": $("#endDate").val(),
            "recruitingCnt": $("#recruitingCnt").val(),
            "file": $("#file").val() // MultipartFile -> form submit?
        };

        $.ajax({
            type: "POST",
            url: "/api/board",
            data: JSON.stringify(data),
            contentType: "application/json;charset=utf8",
            dataType: "text", // default
            success: function(result){
                // location.href=`/board/detail/${result}`;
            },
            fail: function(){
                alert("게시글이 저장되지 않았습니다.");
            }

        });
    },


}