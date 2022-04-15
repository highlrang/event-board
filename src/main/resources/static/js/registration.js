const registration = {

    save: function(boardId, userId){
        let data = {
            "userId": userId,
            "boardId": boardId,
        };
        callJsonAjax("POST", "/api/registration", data, (result) => {
            alert("등록이 완료되었습니다.");
            $("#button-area").html(`<input type="button" onclick="registration.cancel(${result.id}, ${boardId})" class="btn btn-outline-primary" value="참여 취소">`);
            board.addRegistrations(result.registrations);
        });
    },

    cancel: function(id, boardId){
        callAjax("DELETE", `/api/registration/${id}`, {"boardId": boardId}, (result) => {
            alert("등록이 취소되었습니다.");
            $("#button-area").html(`<input type="button" onclick="registration.save(${boardId}, ${result.id})" class="btn btn-outline-primary" value="참여">`);
            board.addRegistrations(result.registrations);
        });
    }
}