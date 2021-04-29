var extList = ['jpg', 'png', 'bmp'];

function getFile() {
  document.getElementById("upfile").click();
}

function sub(obj) {
  let file = obj.value;
  let fileNameArr = file.split("\\");
  let fileName = fileNameArr[fileNameArr.length - 1];
  let ext = fileName.substring(fileName.length - 3);
  
  if(!extList.includes(ext)){
    alert("The file is not of img type.")
    return;
  }

  document.getElementById("uploadBtn").innerHTML = fileName[fileName.length - 1];
  document.myForm.submit();
  event.preventDefault();
}
