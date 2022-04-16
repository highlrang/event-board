const file = {
    save: function(){
        const form = new FormData($("form")[0]);

        $.ajax({
                type: "POST",
                url: "/api/file",
                data: form,
                enctype: "multipart/form-data",
                contentType: false,
                processData: false,
                dataType: "json",
                success: function(result){
                    $("#fileId").val(result.id);
                    $("#fileHtml").html(`<a target="_blank" href="/api/file/${result.id}/download"><img src="${result.path}" alt="${result.name}" class="rounded float-start"></a>`);
                },
                error: function(error){

                }
        });
    }
}