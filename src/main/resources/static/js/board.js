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

    addPaging: function(result) {
        let start = result.number < 10 ? 1 : ((result.number + 1) - ((result.number + 1) / 10)) + 1;
        let last = result.totalPages < 10 ? result.totalPages + 1 : start + 10;

        let pageList = "";
        if (result.number !== 0) {
            pageList += "<a class=\"btn btn-light\" onclick=\"callList(0, field, nowDirection)\"><<</a>"
                + `<a class="btn btn-light" onclick="callList(${result.number - 1}, field, nowDirection)"><</a>`;
        }

        let aClass;
        for (var i = start; i < last; i++) {
            aClass = "btn btn-light";
            if (i === result.number + 1)
                aClass = "btn btn-dark";
            pageList += `<a class="${aClass}" onclick="callList(${i - 1}, field, nowDirection)">${i}</a>`;
        }

        if (result.number !== result.totalPages - 1) {
            pageList += `<a class="btn btn-light" onclick="callList(${result.number + 1}, field, nowDirection)">></a>`
                + `<a class=\"btn btn-light\" onclick=\"callList(${result.totalPages - 1}, field, nowDirection)\">>></a>`;
        }

        $("#pageList").html(pageList);
    }

}