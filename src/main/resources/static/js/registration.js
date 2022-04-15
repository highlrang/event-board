const registration = {

    save: function(boardId, userId){
        let data = {
            "userId": userId,
            "boardId": boardId,
        };
        callJsonAjax("POST", "/api/registration", data, (result) => {
            alert("등록이 완료되었습니다.");
            $("#button-area").html(`<input type="button" onclick="registration.cancel(${result.id}, ${boardId})" class="btn btn-outline-primary" value="참여 취소">`);
            $("#registrationArea").text(result.registrations.length);
        });
    },

    cancel: function(id, boardId){
        callAjax("DELETE", `/api/registration/${id}`, {"boardId": boardId}, (result) => {
            alert("등록이 취소되었습니다.");
            $("#button-area").html(`<input type="button" onclick="registration.save(${boardId}, ${result.id})" class="btn btn-outline-primary" value="참여">`);
            $("#registrationArea").text(result.registrations.length);
        });
    },

    update: function(id, status){
        let data = {
            "id": id,
            "status": status
        }
        callJsonAjax("PUT", "/api/registration/status", data, (result) => {
            board.addRegistrations(result);
        })
    }
}