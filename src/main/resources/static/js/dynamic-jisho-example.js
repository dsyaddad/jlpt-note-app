let exampleIdx = 0;

function addExample() {
    const container = document.getElementById('examples-container');
    
    // Check if there are existing examples from the server and set the index accordingly
    if (exampleIdx === 0) {
        const existingExamples = container.querySelectorAll('.example-block');
        exampleIdx = existingExamples.length;
    }

    const exampleDiv = document.createElement('div');
    exampleDiv.className = 'example-block mb-2 card';
    exampleDiv.innerHTML = `
          <div class="card-header d-flex justify-content-between align-items-center">
            <h6>Example</h6>
            <button type="button" class="btn btn-danger btn-sm" onclick="removeExample(this)">Remove</button>
          </div>
          <div class="card-body">
            <input type="hidden" name="examples[${exampleIdx}].id" >
            <input type="hidden" name="examples[${exampleIdx}].wordId" >
            <input type="text" name="examples[${exampleIdx}].jpSentence" placeholder="Sentence" class="form-control mb-1">
            <input type="text" name="examples[${exampleIdx}].translation" placeholder="Translation" class="form-control mb-1">
            <input type="text" name="examples[${exampleIdx}].note" placeholder="Note" class="form-control mb-1">
          </div>
        `;
    container.appendChild(exampleDiv);
    exampleIdx++;
}

function removeExample(button) {
    const exampleDiv = button.closest('.example-block');
    exampleDiv.remove();
}

document.addEventListener('DOMContentLoaded', function() {
    const container = document.getElementById('examples-container');
    const existingExamples = container.querySelectorAll('.example-block');
    exampleIdx = existingExamples.length;
});
