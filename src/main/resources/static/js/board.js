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

    detail: function(id){
        callAjax("GET", "/api/board/" + id, null,
            (result) => {
                if(result.userInfo.isWriter === true){
                    $("#title").html(`<input type="text" name="title" value="${result.title}" class="form-control">`);
                    $("#content").html(`<textarea name="content" class="form-control" style="width:100%; height: 400px">${result.content}</textarea>`);

                    $("input[name='startDate']").datepicker("setDate", new Date(result.startDate));
                    $("input[name='endDate']").datepicker("setDate", new Date(result.endDate));

                    $("#recruitingCnt").html(`<input type="number" name="recruitingCnt" value="${result.recruitingCnt}" class="form-control">`);
                    $("#file").html("<input type=\"file\" class=\"form-control\">");

                    $("#button-area").html(
                        `<input type="button" onclick="board.update(${result.id})" class="btn btn-outline-primary" value="수정">` +
                        `<input type="button" onclick="board.delete(${result.id}, '${result.boardType}')" class="btn btn-outline-secondary" value="삭제">`
                    );

                }else{
                    $("#title").text(result.title);
                    $("#content").text(result.content);
                    $("#datePeriod").empty();
                    $("#datePeriod").text(result.startDate + " ~ " + result.endDate);
                    result.recruitingCnt !== 0 ? $("#recruitingCnt").text(result.recruitingCnt) : $("#recruitingCnt").text("제한 없음");
                    result.fileId != null ? $("#file").html(`<a target="_blank" class="badge bg-light text-dark" href="/api/board/file-download/${result.fileId}">${result.fileName}</a>`) : $("#file").text("-");

                    let button;
                    result.userInfo.isRegistered === true ?
                        button = `<input type="button" onclick="registration.cancel(${result.userInfo.registrationId}, ${result.id})" class="btn btn-outline-primary" value="참여 취소">`
                        : button = `<input type="button" onclick="registration.save(${result.id}, ${result.userInfo.userId})" class="btn btn-outline-primary" value="참여">`;
                    $("#button-area").html(button);
                }

                /** 공통 */
                $("#writer").text(result.writerName);
                $("#boardType").text(result.boardTypeName);
                $("#views").text(result.views);

                board.addRegistrations(result.registrations);
            }
        );
    },

    addRegistrations: function(data) {
        if(data != null && data.length > 0) {
            $("#registrationArea").html(`<button type="button" class="badge bg-light text-dark" data-bs-toggle="modal" data-bs-target="#registrationList">${data.length}</button>`);

            let modalContent = "";
            $.each(data, function (index, item) {
                // table 형식으로 넣기
                modalContent += `<div>${item.userName} - ${item.statusName}</div>`;
            });

            $("#modalContent").html(modalContent);

        }else{
            $("#registrationArea").text("-");
        }
    },

    list: function(page, field, direction){
        let params = {
            "boardType": boardType,
            "page": page,
            "size": $("#size option:selected").val(),
            "sort": field != null ? field + "," + direction : null
        }

        callAjax("GET", "/api/board/", params,
            (result) => {
                let tbody = "";
                let no;
                $.each(result.content, function(index, item){
                    no = (result.number * result.size) + index + 1;
                    let trClass = "";
                    if(item.topFix === true)
                        trClass = "table-info";
                    let recruitingCnt = item.recruitingCnt !== 0 ? item.recruitingCnt : "제한없음";
                    tbody += `<tr class=\"${trClass}\">`
                        + `<td>${no}</td>`
                        + `<td><a style="text-decoration: none; color: navy" href="/board/${item.id}">${item.title}</a></td>`
                        + `<td>${item.writerName}</td>`
                        + `<td>${item.registrationCnt}/${recruitingCnt}</td>`
                        + `<td>${item.startDate}</td>`
                        + `<td>${item.endDate}</td>`
                        + `<td>${item.views}</td>`
                        + `<td>${item.createdDate}</td>`
                        + "</tr>";
                });
                $("#tbody").html(tbody);


                if(result.totalElements !== 0)
                    this.addPaging(result);
            }
        );
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
    },

    update: function(id){
        const form = new FormData($("form")[0]);

        $.ajax({
            type: "PATCH",
            url: `/api/board/${id}`,
            data: form,
            enctype: "multipart/form-data",
            contentType: false,
            processData: false,
            success: function(){
                board.detail(id);
            },
            error: function(){

            }
        });
    },

    delete: function(id, boardType){
        callAjax("DELETE", `/api/board/${id}`, null, () => {
            location.href=`/board/list/${boardType}`;
        });
    }
}