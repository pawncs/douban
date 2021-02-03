let userGuide = document.querySelector(".userguide");
//点击喜欢的方法
let likes = userGuide.querySelectorAll(".like");
let singers = userGuide.querySelectorAll(".lists>.singer");
let liked = userGuide.querySelector(".liked ul");

let likedNum = 0;
let likedList = [];

likes.forEach(function (currentDiv,index){
    currentDiv.addEventListener('click',function (e){
        switch (currentDiv.innerHTML) {
            case '喜欢':
                like(currentDiv,index);
                break;
            case '已喜欢':
                cancelLike(currentDiv,index)
                break;
        }
    });
});
for(let k = 0;k<liked.children.length;k++){
    liked.children.item(k).addEventListener('click',function (e){
        if(liked.children.item(k).classList.contains("able")){
            cancelLike(liked.children.item(k),k);
        }
    });
    //console.log(liked.children);
}

function like(currentDiv,index){
    if(likedNum === 5){
        return ;
    }
    currentDiv.innerHTML = '已喜欢';
    singers.item(index).classList.add('img2Like');
    //实质
    likedList[likedNum] = singersData[index];
    liked.children.item(likedNum).style.background='url("'+ singersData[index].avatar +'") center no-repeat';
    liked.children.item(likedNum).style.backgroundSize = "contain";
    liked.children.item(likedNum).classList.add("able");
    liked.children.item(likedNum).innerHTML = "<div class='pink'></div>";
    likedNum++;
}

function cancelLike(currentDiv,index){

    let i = 0;
    if(currentDiv.classList.contains("like")){
        for(;i < likedList.length;i++){
            if(likedList[i] === singersData[index]){
            }
        }
    }else{
        i = index;
        for(index = 0;index < singersData.length;index++){
            if(likedList[i] === singersData[index]){
                break;
            }
        }
    }
    if(index<singersData.length){

        likes.item(index).innerHTML = "喜欢";
        singers.item(index).classList.remove('img2Like');
    }

    //实质
    likedList.splice(i,1);

    likedNum--;
    liked.children.item(likedNum).innerHTML = "";
    liked.children.item(likedNum).classList.remove("able");
    liked.children.item(likedNum).style.background='url("img/image 5.png") center no-repeat';
    liked.children.item(likedNum).style.backgroundSize = "contain";

    for(;i<likedNum;i++){
        liked.children.item(i).style.background='url("'+ likedList[i].avatar +'") center no-repeat';
        liked.children.item(i).style.backgroundSize = "contain";
    }
}



let footer = userGuide.querySelector(".footer");
let nowEle = userGuide.querySelector(".none");
footer.addEventListener('click',function (){
    for (let child of userGuide.children) {
        if(child.classList.contains("none")){
            child.style.display = 'flex';
        }else{
            child.style.display = 'none';
        }
    }

    for(let k = 0;k < likedNum;k++){
        let child = nowEle.querySelector("ul").children.item(k);
        child.style.background='url("'+ likedList[k].avatar +'") center no-repeat';
        child.style.backgroundSize = "contain";
        //child.classList.add("able");
        child.innerHTML = "<div class='pink'></div>";
    }
});

let button = nowEle.querySelector(".button");

button.addEventListener('click',function (){
    //todo 进入新的界面，把likedList传递
})

//换一批的逻辑
let change = userGuide.querySelector(".select>.first");
function changeFunction() {
    //todo 更换singersData,下面测试用
    fetch(
        '/singer/random'
    ).then(function (response) {
        return response.json();
    }).then(function (myJson) {
        for(let k  = 0;k<10;k++){
            singersData[k] = myJson[k];
        }

        for(let k = 0;k < 10;k++){
            singers.item(k).querySelector("img").setAttribute('src',singersData[k].avatar) ;
            singers.item(k).querySelector(".text").innerHTML = singersData[k].name;
            likes.item(k).innerHTML = "喜欢";
            singers.item(k).classList.remove('img2Like');
            for(let k2 = 0;k2 < likedList.length;k2++){
                if(singersData[k] === likedList[k2]){
                    likes.item(k).innerHTML = "已喜欢";
                    singers.item(k).classList.add('img2Like');
                    break;
                }
            }
        }
    })

}
change.addEventListener('click',function () {
    changeFunction();
});