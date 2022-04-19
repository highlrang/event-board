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
                if(result.statusCode === "1001") {
                    alert("게시글이 저장되었습니다.");
                    location.href = `/board/${result.body}`;
                }else{
                    result.message && alert(result.message);
                }
            },
            (error) => {
                if(error.statusCode === "1003") {
                    let errRes = JSON.parse(error.responseText);
                    Array.isArray(errRes.body) && alert(errRes.body[0]);
                }else{
                    alert("게시글 저장에 실패하였습니다.");
                }
            }
        );
    },

    detail: function(id){
        callAjax("GET", "/api/board/" + id, null,
            (result) => {
                if(result.statusCode !== "1001"){
                    result.message ? alert(result.message) : alert("게시글을 불러올 수 없습니다.");
                    location.history.back();
                }

                let boardDetail = result.body;
                if (boardDetail.userInfo.isWriter === true) {
                    $("#titleHtml").html(`<input type="text" id="title" value="${boardDetail.title}" class="form-control">`);
                    $("#content").val(`${boardDetail.content}`);

                    $("#startDate").datepicker("setDate", new Date(boardDetail.startDate));
                    $("#endDate").datepicker("setDate", new Date(boardDetail.endDate));

                    $("#recruitingCntHtml").html(`<input type="number" name="recruitingCnt" value="${boardDetail.recruitingCnt}" class="form-control">`);

                    if(boardDetail.file != null) {
                        $("fileId").val(boardDetail.file.id);
                        $("#boardImage").attr('alt', boardDetail.file.name);
                        $("#boardImage").attr('src', boardDetail.file.path);
                        $("#boardImage").css('display', 'block');
                    }else{
                        $("#boardImage").css('display', 'none');
                    }

                    $("#button-area").html(
                        `<input type="button" onclick="board.update(${boardDetail.id})" class="btn btn-outline-primary" value="수정">` +
                        `<input type="button" onclick="board.delete(${boardDetail.id}, '${boardDetail.boardType}')" class="btn btn-outline-secondary" value="삭제">`
                    );

                } else {
                    $("#titleHtml").text(boardDetail.title);
                    $("#contentHtml").empty();
                    $("#contentHtml").css("white-space", "pre");
                    $("#contentHtml").text(boardDetail.content);
                    $("#datePeriod").empty();
                    $("#datePeriod").text(boardDetail.startDate + " ~ " + boardDetail.endDate);
                    boardDetail.recruitingCnt !== 0 ? $("#recruitingCntHtml").text(boardDetail.recruitingCnt) : $("#recruitingCntHtml").text("제한 없음");

                    $("#fileHtml").empty();
                    if(boardDetail.file === null) {
                        $("#fileHtml").text("-");
                    } else {
                        $("#fileId").val(boardDetail.file.id);
                        $("#fileHtml").append(`<img id="boardImage" alt="${boardDetail.file.name}" src="${boardDetail.file.path}" style="width: 80%; height: auto;">`);
                    }

                    let button;
                    boardDetail.userInfo.isRegistered === true ?
                        button = `<input type="button" onclick="registration.cancel(${boardDetail.userInfo.registrationId}, ${boardDetail.id})" class="btn btn-outline-primary" value="참여 취소">`
                        : button = `<input type="button" onclick="registration.save(${boardDetail.id}, ${boardDetail.userInfo.userId})" class="btn btn-outline-primary" value="참여">`;
                    $("#button-area").html(button);
                }

                /** 공통 */
                $("#writer").text(boardDetail.writerName);
                $("#boardType").text(boardDetail.boardTypeName);
                $("#views").text(boardDetail.views);

                if (boardDetail.userInfo.isWriter === true) {
                    board.addRegistrations(boardDetail.registrations);
                } else {
                    $("#registrationArea").text(boardDetail.registrations.length);
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
                let boardList = "";
                $.each(result.content, function(index, item){

                    if(index === 0) {
                        boardList += "<div class='row row-cols-4 mb-5'>";
                    } else if(index % 4 === 0) {
                        boardList += "</div>";
                        boardList += "<div class='row row-cols-4 mb-5'>";
                    }

                    let boardClass = "normalBoard";
                    if(item.topFix === true) boardClass = "specialBoard";
                    let fileName = item.file.name === null ? '기본이미지' : item.file.name;
                    let filePath = item.file.path === null ? '/static/img/default.JPG' : item.file.path;

                    boardList += "<div class='col'>"
                        + `<div class='${boardClass}'>`
                            + "<div>"
                                + `<a href="/board/${item.id}">`
                                    + `<img class='' alt='${fileName}' src='${filePath}'>`
                                + "</a>"
                            + "</div>"

                            + `<div class='mt-3' style='width: 100%; font-weight: bold; overflow: hidden; white-space: nowrap; text-overflow: clip'>${item.title}</div>`

                            + `<div class='mt-3'>${item.startDate} - ${item.endDate}</div>`

                            + "<div class='mt-3'>"
                                + `<span class="float-start" style="font-size: 8px; color: gray">${item.createdDate}</span>`
                                + `<span class="float-end">조회수 : ${item.views}</span>`
                            + "</div>"
                        + "</div>"

                    + "</div>";
                });

                if(result.content.length % 4 !== 0){
                    for(var i=0; i < 4 - (result.content.length % 4); i++){
                        boardList += "<div class='col'></div>";
                    }
                    boardList += "</div>";
                }

                $("#boardList").html(boardList);


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
            pageList += "<a class=\"btn btn-light\" onclick=\"board.list(0, field, nowDirection)\">&laquo;</a>"
                + `<a class="btn btn-light" onclick="board.list(${result.number - 1}, field, nowDirection)">&lsaquo;</a>`;
        }

        let aClass;
        for (var i = start; i < last; i++) {
            aClass = "btn btn-light";
            if (i === result.number + 1)
                aClass = "btn btn-dark";
            pageList += `<a class="${aClass}" onclick="board.list(${i - 1}, field, nowDirection)">${i}</a>`;
        }

        if (result.number !== result.totalPages - 1) {
            pageList += `<a class="btn btn-light" onclick="board.list(${result.number + 1}, field, nowDirection)">&rsaquo;</a>`
                + `<a class=\"btn btn-light\" onclick=\"board.list(${result.totalPages - 1}, field, nowDirection)\">&raquo;</a>`;
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
            (result) => {
                    if(result.statusCode === "1001") {
                        alert("수정이 완료되었습니다.");
                        location.href=`/board/${id}`;
                    }else{
                        result.message && alert(result.message);
                    }
                },
            (error) => {
                    if(error.statusCode === "1003"){
                        let errRes = JSON.parse(error.responseText);
                        Array.isArray(errRes.body) && alert(errRes.body[0]);
                    }else{
                       alert("게시글 수정에 실패하였습니다.");
                    }
                }
            );
    },

    delete: function(id, boardType){
        callAjax("DELETE", `/api/board/${id}`, null, (result) => {
            if(result.statusCode === "1001")
                location.href = `/board/list/${boardType}`;

            alert("게시글 삭제에 실패했습니다.");
        });
    }
}