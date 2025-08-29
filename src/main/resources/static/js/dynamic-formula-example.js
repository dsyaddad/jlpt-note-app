let formulaIdx = 0;

function addFormula() {
    const container = document.getElementById('formulas-container');
    const formulaDiv = document.createElement('div');
    formulaDiv.className = 'formula-block mb-3 card';
    formulaDiv.innerHTML = `
          <div class="card-header">
            <h5>Formula</h5>
          </div>
          <div class="card-body">
            <input type="text" name="formulas[${formulaIdx}].subSection" placeholder="Sub Section" class="form-control mb-1" required>
            <input type="text" name="formulas[${formulaIdx}].pattern" placeholder="Pattern" class="form-control mb-1" required>
            <input type="text" name="formulas[${formulaIdx}].subFunction" placeholder="Sub Function" class="form-control mb-1">
            <input type="text" name="formulas[${formulaIdx}].subUseWhen" placeholder="Sub Use When" class="form-control mb-1">
            <textarea name="formulas[${formulaIdx}].subNote" placeholder="Sub Note" class="form-control mb-1"></textarea>
            <div id="examples-container-${formulaIdx}"></div>
            <button type="button" onclick="addExample(${formulaIdx})" class="btn btn-success">Add Example</button>
            <hr>
          </div>
        `;
    container.appendChild(formulaDiv);
    formulaIdx++;
}

function addExample(formulaIndex) {
    const container = document.getElementById(`examples-container-${formulaIndex}`);
    if (!container.exampleIdx) container.exampleIdx = 0;
    const exampleDiv = document.createElement('div');
    exampleDiv.className = 'example-block mb-2 card';
    exampleDiv.innerHTML = `
          <div class="card-header">
            <h6>Example</h6>
          </div>
          <div class="card-body">
            <input type="text" name="formulas[${formulaIndex}].examples[${container.exampleIdx}].sampleKanji" placeholder="Sample Kanji" class="form-control mb-1">
            <input type="text" name="formulas[${formulaIndex}].examples[${container.exampleIdx}].sampleNonKanji" placeholder="Sample Non Kanji" class="form-control mb-1">
            <input type="text" name="formulas[${formulaIndex}].examples[${container.exampleIdx}].meaning" placeholder="Meaning" class="form-control mb-1">
            <input type="text" name="formulas[${formulaIndex}].examples[${container.exampleIdx}].note" placeholder="Note" class="form-control mb-1">
            <hr>
          </div>
        `;
    container.appendChild(exampleDiv);
    container.exampleIdx++;
}