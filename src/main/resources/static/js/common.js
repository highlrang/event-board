function callAjax(method, url, params, callback){
    $.ajax({
        type: method,
        url: url,
        data: params,
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        success: function(data){
            callback(data);
        },
        error: function(error){

        }
    });
}