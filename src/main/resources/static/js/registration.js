const registration = {

    save: function(boardId, userId){
        let data = {
            "userId": userId,
            "boardId": boardId,
        };
        callJsonAjax("POST", "/api/registration", data,
            (result) => {
                if(result.statusCode === "1001") {
                    alert("등록이 완료되었습니다.");
                    $("#button-area").html(`<input type="button" onclick="registration.cancel(${result.body.id}, ${boardId})" class="btn btn-outline-primary" value="참여 취소">`);
                    $("#registrationArea").text(result.body.registrations.length);
                }else{
                    alert("등록에 실패하였습니다.");
                }
            },
            () => {
                alert("등록에 실패하였습니다.");
            }
        );
    },

    cancel: function(id, boardId){
        callAjax("DELETE", `/api/registration/${id}`, {"boardId": boardId},
            (result) => {
                if (result.statusCode === "1001") {
                    alert("등록이 취소가 완료되었습니다.");
                    $("#button-area").html(`<input type="button" onclick="registration.save(${boardId}, ${result.body.id})" class="btn btn-outline-primary" value="참여">`);
                    $("#registrationArea").text(result.body.registrations.length);
                } else {
                    alert("등록 취소에 실패하였습니다.");
                }
            },
            () => {
                alert("등록 취소에 실패하였습니다.");
            });
    },

    update: function(id, status){
        let data = {
            "id": id,
            "status": status
        }
        callJsonAjax("PUT", "/api/registration/status", data,
            (result) => {
                if (result.statusCode === "1001")
                    board.addRegistrations(result.body);
                else
                    alert("실패하였습니다.");
            },
            () => {
                alert("실패하였습니다.");
            })
    }
}