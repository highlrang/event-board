const user = {
    checkPassword: function(){
        let password = $("#password").val();
        let passwordCheck = $("#passwordCheck").val();

        if(!new RegExp("^[a-zA-Z0-9]{8,}$").test(password)){
            $("#errorMsg").text("비밀번호는 8자 이상, 숫자º영문 조합이어야합니다.");
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
        callAjax("GET", "/api/user/validate-user-id", { "email" : $("#email").val() },
            (result) => {
                if(result.statusCode !== "1001") {
                    $("#errorMsg").text(result.message);
                }else {
                    $("#id-check").val("완료");
                    $("#id-check").attr("disabled", true);
                    $("#id-check").attr("class", "btn btn-outline-secondary");
                    $("#errorMsg").text("");
                    isIdPossible = true;
                }
            });
    },

    save: function(){
        let data = {
            "email": $("#email").val(),
            "nickName": $("#nickName").val(),
            "password": $("#password").val(),
            "isAdmin": $("#isAdmin").prop("checked")
        }

        callJsonAjax("POST", "/api/user", data, (result) => {
            if(result.statusCode === "1001"){
                alert("회원가입이 완료되었습니다.");
                location.href = "/login";
            }else if(result.statusCode === "2002"){
                alert(result.message);
            }else{
                alert("회원가입에 실패하였습니다.");
            }

        });
    }

}