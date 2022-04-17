const board = {

    save: function () {
        let data = {
            "boardType": $("#boardType").val(),
            "writerId": $("#writerId").val(),
            "title": $("#title").val(),
            "content": $("#content").val(),
            "startDate": $("#startDate").val(),
            "endDate": $("#endDate").val(),
            "recruitingCnt": $("#recruitingCnt").val(),
            "fileId": $("#fileId").val()
        }

        callJsonAjax("POST", "/api/board", data,
            (result) => {
                alert("게시글이 저장되었습니다.");
                location.href=`/board/${result.body}`;
            },
            (error) => {
                let errRes = JSON.parse(error.responseText);
                Array.isArray(errRes.body) && alert(errRes.body[0]);
            }
        );
    },

    detail: function(id){
        callAjax("GET", "/api/board/" + id, null,
            (result) => {
                if (result.userInfo.isWriter === true) {
                    $("#titleHtml").html(`<input type="text" id="title" value="${result.title}" class="form-control">`);
                    $("#contentHtml").html(`<textarea id="content" class="form-control" style="width:100%; height: 400px">${result.content}</textarea>`);

                    $("#startDate").datepicker("setDate", new Date(result.startDate));
                    $("#endDate").datepicker("setDate", new Date(result.endDate));

                    $("#recruitingCntHtml").html(`<input type="number" name="recruitingCnt" value="${result.recruitingCnt}" class="form-control">`);

                    if(result.file != null) {
                        $("fileId").val(result.file.id);
                        $("#boardImage").attr('alt', result.file.name);
                        $("#boardImage").attr('src', result.file.path);
                        $("#boardImage").css('display', 'block');
                    }else{
                        $("#boardImage").css('display', 'none');
                    }

                    $("#button-area").html(
                        `<input type="button" onclick="board.update(${result.id})" class="btn btn-outline-primary" value="수정">` +
                        `<input type="button" onclick="board.delete(${result.id}, '${result.boardType}')" class="btn btn-outline-secondary" value="삭제">`
                    );

                } else {
                    $("#titleHtml").text(result.title);
                    $("#contentHtml").text(result.content);
                    $("#datePeriod").empty();
                    $("#datePeriod").text(result.startDate + " ~ " + result.endDate);
                    result.recruitingCnt !== 0 ? $("#recruitingCntHtml").text(result.recruitingCnt) : $("#recruitingCntHtml").text("제한 없음");

                    $("#fileHtml").empty();
                    if(result.file === null) {
                        $("#fileHtml").text("-");
                    } else {
                        $("#fileId").val(result.file.id);
                        $("#fileHtml").append(`<img id="boardImage" alt="${result.file.name}" src="${result.file.path}" class="rounded float-start">`);
                    }

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

                if (result.userInfo.isWriter === true) {
                    board.addRegistrations(result.registrations);
                } else {
                    $("#registrationArea").text(result.registrations.length);
                }
            })
    },

    addRegistrations: function(data) {
        if(data != null && data.length > 0) {
            $("#registrationArea").html(`<button type="button" class="badge bg-light text-dark" data-bs-toggle="modal" data-bs-target="#registrationList">${data.length}</button>`);

            let modalContent = "<table class=\"table\" style=\"text-align: center\"><tbody>";
            $.each(data, function (index, item) {
                let rClass = item.status === 'APPLY' ? 'btn btn-outline-primary' : 'btn btn-outline-secondary';
                let rValue = item.status === 'APPLY' ? '수락' : '취소';
                let rClick = item.status === 'APPLY' ? `registration.update(${item.id}, 'OK')` : `registration.update(${item.id}, 'APPLY')`;

                modalContent += "<tr>"
                    + `<td>${item.userName}</td>`
                    + `<td>${item.statusName}</td>`
                    + `<td><input type=\"button\" class=\"${rClass}\" value=\"${rValue}\" onclick=\"${rClick}\"></td>`
                    + "</tr>";
            })

            modalContent += "</tbody></table>";
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
        let data = {
            "title": $("#title").val(),
            "content": $("#content").val(),
            "recruitingCnt": $("#recruitingCnt").val(),
            "startDate": $("#startDate").val(),
            "endDate": $("#endDate").val(),
            "fileId": $("#fileId").val()
        };

        callJsonAjax("PATCH", `/api/board/${id}`, data,
            () => {
                alert("수정이 완료되었습니다.");
                location.href=`/board/${id}`;
                },
            (error) => {
                let errRes = JSON.parse(error.responseText);
                Array.isArray(errRes.body) && alert(errRes.body[0]);
                }
            );
    },

    delete: function(id, boardType){
        callAjax("DELETE", `/api/board/${id}`, null, () => {
            location.href=`/board/list/${boardType}`;
        });
    }
}