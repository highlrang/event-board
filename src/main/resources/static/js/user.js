const user = {
    checkPassword: function(){
        let password = $("#password").val();
        let passwordCheck = $("#passwordCheck").val();

        if(password.length < 8){ // || 정규식으로 검증 추가
            $("#errorMsg").text("비밀번호는 8자 이상, 숫자º영문º특수문자 중 두 가지 조합이어야합니다.");
            return false;
        }

        if (password !== passwordCheck){
            $("#errorMsg").text("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return false;
        }

        $("#errorMsg").text("");
        return true;
    },

    checkId: function(){
        callAjax("POST", "/api/user/validate-user-id", { "userId" : $("#userId").val() },
            (result) => {
                console.log(result);
                if(result.code !== "1001") {
                    $("#errorMsg").text(result.message);
                }else {
                    $("#errorMsg").text("");
                    isIdPossible = true;
                }
            });
    },

    save: function(){
        let data = {
            "userId": $("#userId").val(),
            "nikName": $("#name").val(),
            "password": $("#password").val(),
            "isAdmin": false
        }

        callAjax("POST", "/api/user", data, () => {
            location.href = "/login";
        });
    },

}