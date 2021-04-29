function getFile() {
  console.log("elll");
  document.getElementById("upfile").click();
}

function sub(obj) {
  console.log('sub');
  var file = obj.value;
  console.log(file);
  var fileName = file.split("\\");
  console.log(fileName);
  document.getElementById("uploadBtn").innerHTML = fileName[fileName.length - 1];
  document.myForm.submit();
  event.preventDefault();
}
