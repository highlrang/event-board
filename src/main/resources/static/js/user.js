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
        callJsonAjax("POST", "/api/user/validate-user-id", { "userId" : $("#userId").val() },
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
            "userId": $("#userId").val(),
            "nickName": $("#nickName").val(),
            "password": $("#password").val(),
            "isAdmin": false //
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
    },

    info: function(){
        callAjax("GET", "/api/user/info", null, (result)=>{
            if(result.statusCode === "2005"){
                $("#content").html("<a href=\"/logout\">로그아웃</a>");
            }else{
                $("#content").html("<a href=\"/login\">로그인</a>");
            }
        });
    }

}