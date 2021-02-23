let login_form = document.querySelector("form");

let login_button = login_form.querySelector("button");

let login_error_information = login_form.querySelector(".hide");

let toRegister = login_form.querySelector(".title .register span");

//表单的内容
let formData;
let inputs = login_form.querySelectorAll("input");
let name_input = inputs[0];
let password_input = inputs[1];
let select_input = inputs[2];

login_button.addEventListener("click", function () {
    //todo
    formData = JSON.stringify({
        name:name_input.value,
        password:password_input.value,
        isAutoLogin:select_input.value
    })

    login_error_information.style.display = 'flex';
});

toRegister.addEventListener("click",function (){
    //todo 跳转到注册页面
});
