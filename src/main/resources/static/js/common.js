
/** GET, DELETE */
function callAjax(method, url, params, successFn, errorFn){
    $.ajax({
        type: method,
        url: url,
        data: params,
        dataType: "json",
        success: function(data){
            successFn(data);
        },
        error: function(error){
            errorFn(error);
        }
    });
}

/** POST, PATCH */
function callJsonAjax(method, url, data, successFn, errorFn){
    $.ajax({
        type: method,
        url: url,
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data){
            successFn(data);
        },
        error: function(error){
            errorFn(error);
        }
    });
}