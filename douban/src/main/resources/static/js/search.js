let searchResponse = document.querySelector(".searchResponse");

let input = document.querySelector(".search input");

let c = null;

function searchClick() {
    //console.log("123");
    //todo 经过了点击操作，获取了c
    fetch(
        "/searchContent?keyword=" + input.value
    ).then(function (response) {
            return  response.json();
    }).then(data=>{
        //console.log(c,data);
        c = data;

        searchResponse.innerHTML = "";
        if (c == null) {
            console.log("kong")
            return;
        }
        let songs = c.songs.content;
        //console.log(songs);
        for (let i = 0; i < songs.length; i++) {
            searchResponse.innerHTML +=
                "<div class='searchOne searchOne-" + i + "'>" +
                "</div>";
            let searchOne = searchResponse.querySelectorAll(".searchOne")[i];
            searchOne.innerHTML = "<div class='img'></div><div class='name'></div>";
            searchOne.querySelector(".name").innerHTML = songs[i].name;
            if (songs[i].cover != null) {
                let img = searchOne.querySelector(".img");
                img.innerHTML = "<img src='" + songs[i].cover + "'/>";
            }
        }
    });


}

function keyUp(e){
    searchClick();
}
input.onkeyup = keyUp;

//input.addEventListener("click", searchClick);