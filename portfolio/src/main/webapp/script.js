// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

function openLink(link){
  window.open(link);
}

function getComments() {
  const query = '/data?maxComments='.concat(document.getElementById("max-comments").value);
  fetch(query).then(response => response.json()).then((jsonCommentMap) => {
    const commentList = document.createElement('dl');
    document.getElementById("display-comments").innerHTML = '';
    document.getElementById("display-comments").appendChild(commentList);

    Object.keys(jsonCommentMap).forEach((name)=> {
      commentList.append(userCommentsAsList(name, jsonCommentMap[name]));     
    });
  });
}

function deleteComments() {
  const request = new Request('/delete-data', {method:'Post'});
  fetch(request).then(getHello());
}

//formats comments as sublists per specific user name
function userCommentsAsList(name, comments) {
  const user = document.createElement('dt');
  user.innerText = name.concat(' has said:');
  console.log(comments);
  comments.forEach((comment)=> {
    user.appendChild(createElementFrom(comment));
  });
  return user;
}

function createElementFrom(comment) {
  const liElement = document.createElement('dd');
  //formats comment string and corresponding checkbox to be one line
  liElement.innerText = comment.propertyMap.value.concat("    ");
  liElement.innerHTML += createDeleteCheckBox(comment.key);
  return liElement;
}

function createDeleteCheckBox(key) {
  return '<input type="checkbox" value='.concat('"' + key + '">')
}
