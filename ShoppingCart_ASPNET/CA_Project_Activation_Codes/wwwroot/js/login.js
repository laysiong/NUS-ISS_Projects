window.onload = function () {
    let usernameElem = document.getElementById("username");
    let userpwdElem = document.getElementById("password");
    let submitElem = document.getElementById("submit_btn");

    // close popup and overlay
    document.getElementById("close_btn")?.addEventListener("click", (event) => {
        let overlayElem = document.getElementById("overlay");
        let popupElem = document.getElementById("popup");
        overlayElem.style.display = "none";
        popupElem.style.display = "none";
    });

    // check username null
    submitElem.addEventListener("click", (event) => {
        let username = usernameElem.value;
        let password = userpwdElem.value;
        // if username or password blank; pormpt out reminder
        if (username.length == 0 || password.length == 0) {
            alert("username and password should not be null, please enter again.");
        }
    });
}