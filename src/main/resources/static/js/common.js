
/** GET, DELETE */
function callAjax(method, url, params, callback){
    $.ajax({
        type: method,
        url: url,
        data: params,
        dataType: "json",
        success: function(data){
            callback(data);
        },
        error: function(error){

        }
    });
}

/** POST, PATCH */
function callJsonAjax(method, url, data, callback){
    $.ajax({
        type: method,
        url: url,
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data){
            callback(data);
        },
        error: function(error){

        }
    });
}