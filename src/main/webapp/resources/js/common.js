let numberOfPages;
let currentPage = 1;
let currentPageIndex = 1;
let lastPage;
let recordsPerPage;
let currentURL;
let entity;
let check = {
    products: [],
    sum: 0
};
let report = {
    checks: [],
    totalSum: 0
};
let allMessages = {};

const domLoadedListener = () => {
    currentURL = window.location.href;
    processPagination();

    processEntityView();

    addHandlers();

    document.getElementById('productSearch').onkeyup = productSearchHandler;

    document.getElementById('checkSearch').onkeyup = checkSearchHandler;
};
document.addEventListener("DOMContentLoaded", domLoadedListener);

const addHandlers = () => {
    let addFormButtons = document.getElementsByClassName('add-form');
    if (addFormButtons.length === 1) {
        setTimeout(addHandlers, 50);
        return;
    }

    let deleteButtons = document.getElementsByClassName('delete');
    Array.from(deleteButtons).forEach((deleteButton) => {
        deleteButton.onclick = deleteEntityHandler;
    });

    Array.from(addFormButtons).forEach((addButton) => {
        addButton.onclick = editEntityHandler;
    });
};

const saveEmployee = (event) => {
    let employeeData = event.target.parentElement;
    let employee = {
        id: Number.parseInt(employeeData.querySelector('input[id="id"]').value),
        firstName: $('#firstName').val(),
        lastName: $('#lastName').val(),
        email: $('#email').val(),
        salary: Number.parseFloat($('#salary').val()),
        position: $('#position').val()
    };

    $.ajax('/app/employees', {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        data: JSON.stringify(employee)
    }).done((data) => {
        if (data.errors === undefined) {
            window.location.href = '/app/employees/view';
        } else {
            allMessages = data.messages;
            let formContent = $('.add-form-content').get(0);
            formContent.innerHTML = '';
            renderEntity(data, 'form');
            saveHandler();
        }
    }).fail(function (jqXHR) {
        let html = document.getElementsByTagName('HTML')[0];
        html.innerHTML = jqXHR.responseText;
    });
};

const saveCheck = (event) => {
    let checkId = event.target.parentElement.querySelector('input[id="id"]').value;
    let employeeId = $('#employee-id').val();
    checkId.id = checkId === '' ? 0 : Number.parseInt(checkId);
    check.date = formatForJava(new Date($('#date').val()));
    check.sum = Number.parseFloat($('#sum').val());
    check.employee = {
        id: employeeId === '' ? 0 : Number.parseInt(employeeId),
        firstName: $('#employee-fn').val(),
        lastName: $('#employee-ln').val()
    };
    check.status = $('#status').val();

    let method = checkId === '' ? 'POST' : 'PUT';
    $.ajax('/app/checks', {
        method: method,
        data: JSON.stringify(check),
        headers: {'Content-Type': 'application/json'}
    }).done((data) => {
        if (data.errors === undefined) {
            window.location.href = '/app/checks/view';
        } else {
            allMessages = data.messages;

            let dataCheck = data.check;
            dataCheck.products.forEach(p => {
                let unvPr = data.unavailableProducts.filter(up => up.id === p.id);
                if (unvPr[0])
                    p.quantityOnStock = unvPr[0].quantityOnStock;
            });
            check = dataCheck;

            let formContent = $('.add-form-content').get(0);
            formContent.innerHTML = '';
            let div = $('.add-form').get(0).parentElement.nextElementSibling;
            div.innerHTML = '';
            let conFluid = $('.container-fluid').get(0);
            conFluid.classList.remove('container-fluid');
            conFluid.classList.add('container');

            renderEntity(data, 'form');
            saveHandler();
            if (data.errors.unavailableProducts !== undefined) {
                let productUnavailableModal = $('#productUnavailable').get(0);
                let modalBody = productUnavailableModal.querySelector('.modal-body');
                modalBody.textContent = data.errors.unavailableProducts;
                let unavailableProducts = data.unavailableProducts;
                let listElem = document.createElement('UL');
                for (let i = 0; i < unavailableProducts.length; i++) {
                    listElem.insertAdjacentHTML('beforeEnd', `
                        <li>${unavailableProducts[i].title}: ${unavailableProducts[i].code}</li>
                    `);
                }
                modalBody.appendChild(listElem);
                $(productUnavailableModal).modal('toggle');
            }
        }
    }).fail(function (jqXHR) {
        let html = document.getElementsByTagName('HTML')[0];
        html.innerHTML = jqXHR.responseText;
    });
};

const saveProduct = (event) => {
    let fileInput = document.querySelector('input[type="file"]');
    let files = fileInput.files;

    let image = '';
    let reader = new FileReader();
    reader.onload = () => {
        image = reader.result;
        if (image === 'data:') {
            image = '';
        } else {
            let index = image.lastIndexOf(',');
            image = image.slice(index + 1);
        }

        let inputId = event.target.parentElement.querySelector('input[id="id"]').value;
        if (inputId !== '' && image === '') {
            image = $('#img').val();
        }
        let parsedId = inputId === '' ? 0 : Number.parseInt(inputId);
        let product = {
            id: parsedId,
            title: $('#title').val(),
            code: Number.parseInt($('#code').val()),
            price: Number.parseFloat($('#price').val()),
            quantityType: $('#quantityType').val(),
            boughtQuantity: 0,
            quantityOnStock: Number.parseInt($('#quantityOnStock').val()),
            image: image,
        };
        let method = inputId === '' ? 'POST' : 'PUT';
        $.ajax('/app/products',
            {
                method: method,
                headers: {'Content-Type': 'application/json'},
                data: JSON.stringify(product)
            }).done((data) => {
            if (data.errors === undefined) {
                window.location.href = '/app/products/view';
            } else {
                allMessages = data.messages;
                let formContent = $('.add-form-content').get(0);
                formContent.innerHTML = '';
                productForm(data.product, data.messages, formContent, data.errors);
                saveHandler();
            }
        }).fail(function (jqXHR) {
            let html = document.getElementsByTagName('HTML')[0];
            html.innerHTML = jqXHR.responseText;
        });
    };
    let f = files.length === 0 ? new Blob(['']) : files[0];
    reader.readAsDataURL(f);
};

const saveReport = (event) => {
    let reportId = event.target.parentElement.querySelector('input[id="id"]').value;
    report.id = reportId === '' ? 0 : Number.parseInt(reportId);
    report.sinceDate = formatForJava(new Date($('#since-date').val()));
    report.untilDate = formatForJava(new Date($('#until-date').val()));
    report.creationDate = formatForJava(new Date($('#creation-date').val()));
    report.totalSum = Number.parseFloat($('#totalSum').val());
    report.type = $('#type').val();

    let method = reportId === '' ? 'POST' : 'PUT';
    $.ajax('/app/reports', {
        method: method,
        data: JSON.stringify(report),
        headers: {'Content-Type': 'application/json'}
    }).done((data, textStatus) => {
        if (data.errors === undefined) {
            window.location.href = '/app/reports/view';
        } else {
            allMessages = data.messages;
            let formContent = $('.add-form-content').get(0);
            formContent.innerHTML = '';
            let div = $('.add-form').get(0).parentElement.nextElementSibling;
            div.innerHTML = '';

            renderEntity(data, 'form');
            saveHandler();
        }
    }).fail(function (jqXHR) {
        let html = document.getElementsByTagName('HTML')[0];
        html.innerHTML = jqXHR.responseText;
    });
};

const productSearchHandler = (event) => {
    let uri = '/app/products';
    let inputVal = event.target.value;
    if (inputVal.match(/^\d+$/g) != null) {
        uri += `/findByCode?code=${inputVal}`;
    } else if (inputVal === '' && entity === 'checks') {
        let checkForm = $('.add-form-content > .card > .card-body > form').get(0);
        let checkId = checkForm.querySelector('input[id="id"]').value;
        if (checkId !== '')
            uri += `/findByCheckId?checkId=${checkId}`;
        else
            uri += '';
    } else if (inputVal === '' && entity === 'products') {
        uri += '';
    } else {
        uri += `/findByTitle?title=${inputVal}`;
    }

    $.ajax(uri, {method: 'GET'}).done((data) => {
        allMessages = data.messages;
        let regex = `\\/${entity}\\/view(\\?language=\\w*)?`;
        let regexp = new RegExp(regex, 'g');
        if (currentURL.match(regexp)) {
            if (entity === 'products') {
                if (inputVal === '' && data.products.length > recordsPerPage) {
                    data = {
                        products: data.products.slice(0, recordsPerPage),
                        messages: data.messages
                    }
                }
                renderEntity(data, 'table');
                addHandlers();
            } else {
                let div = $('.container-fluid').get(0).firstElementChild.lastElementChild.lastElementChild;
                let table = div.querySelector('table');
                if (table)
                    div.removeChild(table);

                let product = data.product != null ? Array.of(data.product) : [];
                product = data.products != null ? data.products : product.length > 0 ? product : [];
                checkProductsTable(product, data.messages, div);
            }
        } else {
            $('.container').get(2).children[0].innerHTML = "";
            let row = $('.container').get(1).children[0];
            row.innerHTML = "";
            let foundProducts = data.product != null ? Array.of(data.product) : [];
            foundProducts = data.products != null ? data.products : foundProducts.length > 0 ? foundProducts : [];
            if (foundProducts.length === 0) {
                row.insertAdjacentHTML("beforeEnd", `<div class="col-sm-4"></div>
                                                    <div class="col-sm-4"><p>${allMessages.noResults}</p></div>
                                                    <div class="col-sm-4"></div>`);
            } else {
                let resolvedData = {
                    products: foundProducts,
                    messages: data.messages
                };

                renderEntity(resolvedData, 'card');
            }
        }
    }).fail(function (jqXHR) {
        let html = document.getElementsByTagName('HTML')[0];
        html.innerHTML = jqXHR.responseText;
    });
};

const checkSearchHandler = (event) => {
    let uri = '/app/checks';
    let inputVal = event.target.value;
    if (inputVal === '') {
        if (entity === 'reports') {
            let reportForm = $('.add-form-content > .card > .card-body > form').get(0);
            let reportId = reportForm.querySelector('input[id="id"]').value;
            uri += reportId !== '' ? `/findAllByReportId?reportId=${reportId}` : '';
        } else
            uri += '';
    } else {
        uri += `/findAllByDate?date=${inputVal}`;
    }

    $.ajax(uri, {method: 'GET'}).done((data) => {
        allMessages = data.messages;
        if (entity === 'checks') {
            if (inputVal === '' && data.checks.length > recordsPerPage) {
                data = {
                    checks: data.checks.slice(0, recordsPerPage),
                    messages: data.messages
                }
            }
            renderEntity(data, 'table');
            addHandlers();
        } else {
            let div = $('.container').get(0).firstElementChild.lastElementChild.lastElementChild;
            let table = div.querySelector('table');
            if (table)
                div.removeChild(table);

            let reportChecks = data.check != null ? Array.of(data.check) : [];
            reportChecks = data.checks != null ? data.checks : reportChecks.length > 0 ? reportChecks : [];
            reportChecksTable(reportChecks, data.messages, div);
        }
    }).fail(function (jqXHR) {
        let html = document.getElementsByTagName('HTML')[0];
        html.innerHTML = jqXHR.responseText;
    });
};

const onReportTypeChange = (event) => {
    $.ajax('/app/reports/findAllByType?type=' + event.target.value, {method: 'GET'})
        .done((data) => {
            allMessages = data.messages;
            let report = data.report != null ? Array.of(data.report) : [];
            report = data.reports != null ? data.reports : report.length > 0 ? report : [];
            let elem = document.getElementsByClassName('well')[0];
            if (elem.firstElementChild.tagName === 'TABLE')
                elem.removeChild(elem.firstElementChild);
            reportsTable(report, data.messages, elem);
            addHandlers();
        }).fail(function (jqXHR) {
        let html = document.getElementsByTagName('HTML')[0];
        html.innerHTML = jqXHR.responseText;
    });
};

const processPagination = () => {
    if (currentPage > 1)
        return;

    let pagination = document.getElementsByClassName('pagination');
    if (pagination.length > 0) {
        pagination = pagination[0];
        if (numberOfPages <= 5) {
            pagination.children[pagination.childElementCount - 2].classList.add('hidden');

            for (let i = 0; i < 5 - numberOfPages; i++) {
                pagination.children[pagination.childElementCount - (3 + i)].classList.add('hidden');
            }
        }
    }
};

const processEntityView = () => {
    let pattern = `\\/${entity}\\/view(\\?language=\\w*)?`;
    let regexp = new RegExp(pattern, 'g');

    if (currentURL.match(regexp)) {
        let xhr = new XMLHttpRequest();
        xhr.open('GET', `/app/${entity}?recordsPerPage=${recordsPerPage}&page=${currentPage}`, true);
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== 4)
                return;

            let data = JSON.parse(xhr.response);
            allMessages = data.messages;
            renderEntity(data, 'table');
        };
        xhr.send();
    }
};

const paginationRequest = () => {
    let slashIndex = 0;
    for (let i = 0; i < 3; i++, slashIndex++) {
        slashIndex = currentURL.indexOf('/', slashIndex);
        if (!((i + 1) < 3))
            slashIndex--;
    }

    let currentUri = currentURL.slice(slashIndex);
    let xhr = new XMLHttpRequest();
    currentUri = currentUri.replace(/\?recordsPerPage=\d+#?(&page=\d+)?/, '');
    let url = `${currentUri}?recordsPerPage=${recordsPerPage}&page=${currentPage}`;
    if (currentUri.match(/.*\/view.*/g) == null || currentUri === '/') {
        xhr.open('GET', `/app/products?recordsPerPage=${recordsPerPage}&page=${currentPage}`, true);
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== 4)
                return;

            $('.container').get(2).children[0].innerHTML = "";
            let row = $('.container').get(1).children[0];
            row.innerHTML = "";
            let data = JSON.parse(xhr.response);
            allMessages = data.messages;
            renderEntity(data, 'card');
        };
        xhr.send();
        return;
    } else {
        url = url.replace(/\/?view#?/, '');
    }
    xhr.open('GET', url, true);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== 4)
            return;

        let data = JSON.parse(xhr.response);
        allMessages = data.messages;
        renderEntity(data, 'table');
        addHandlers();
    };
    xhr.send();
};

const handleClickPage = (event) => {
    let elem = event.target.tagName === "A" ? event.target.parentNode : event.target;
    let pageVal = Number.parseInt(elem.firstChild.textContent);
    let index = Number.parseInt(elem.id.slice(-1));
    if (currentPage < pageVal)
        next(pageVal, index);
    else
        previous(pageVal, index);
};

const next = (nextPageParam, nextPageIndexParam) => {
    if (currentPage === lastPage)
        return;

    let nextPage;
    let nextPageIndex;
    if (nextPageParam !== undefined && nextPageIndexParam !== undefined) {
        nextPage = nextPageParam;
        nextPageIndex = nextPageIndexParam;
    } else {
        nextPage = currentPage + 1;
        nextPageIndex = currentPageIndex + 1;
    }

    if (numberOfPages > 5) {
        if (nextPageIndex > 3 && numberOfPages > 5 && lastPage - 2 > nextPage) {
            document.getElementsByClassName("pagination")[0].children[1].classList.remove("hidden");
            document.getElementById("page-" + currentPageIndex).classList.remove("active");
            document.getElementById("page-3").classList.add("active");
            assignPages(nextPage);
            currentPageIndex = 3;
        } else {
            if (lastPage - 2 <= nextPage) {
                document.getElementsByClassName("pagination")[0].children[1].classList.remove("hidden");
                document.getElementsByClassName("pagination")[0].lastElementChild.previousElementSibling.classList.add("hidden");
                assignPages(lastPage - 2);
            }
            if (nextPage === lastPage - 2)
                nextPageIndex = 3;
            else if (nextPage === lastPage - 1)
                nextPageIndex = 4;
            document.getElementById("page-" + currentPageIndex).classList.remove("active");
            document.getElementById("page-" + nextPageIndex).classList.add("active");
            currentPageIndex = nextPageIndex;
        }
    } else {
        document.getElementById("page-" + currentPageIndex).classList.remove("active");
        document.getElementById("page-" + nextPageIndex).classList.add("active");
        currentPageIndex = nextPageIndex;
    }
    currentPage = nextPage;
    paginationRequest();
};

const previous = (previousPageParam, previousPageIndexParam) => {
    if (currentPage === 1)
        return;

    let previousPage;
    let previousPageIndex;
    if (previousPageParam !== undefined && previousPageIndexParam !== undefined) {
        previousPage = previousPageParam;
        previousPageIndex = previousPageIndexParam;
    } else {
        previousPage = currentPage - 1;
        previousPageIndex = currentPageIndex - 1;
    }

    if (numberOfPages > 5) {
        if (previousPageIndex < 3 && previousPage > 3 && previousPage < lastPage - 2) {
            document.getElementsByClassName("pagination")[0].lastElementChild.previousElementSibling.classList.remove("hidden");
            document.getElementById("page-" + currentPageIndex).classList.remove("active");
            document.getElementById("page-3").classList.add("active");
            assignPages(previousPage);
            currentPageIndex = 3;
        } else {
            if (previousPage === 3 || previousPage === lastPage - 2)
                previousPageIndex = 3;
            else if (previousPage === 2)
                previousPageIndex = 2;
            document.getElementById("page-" + currentPageIndex).classList.remove("active");
            document.getElementById("page-" + previousPageIndex).classList.add("active");
            if (previousPage >= lastPage - 2) {
                assignPages(lastPage - 2);
            } else {
                document.getElementsByClassName("pagination")[0].children[1].classList.add("hidden");
                document.getElementsByClassName("pagination")[0].lastElementChild.previousElementSibling.classList.remove("hidden");
                assignPages(3);
            }
            currentPageIndex = previousPageIndex;
        }
    } else {
        document.getElementById("page-" + currentPageIndex).classList.remove("active");
        document.getElementById("page-" + previousPageIndex).classList.add("active");
        currentPageIndex = previousPageIndex;
    }
    currentPage = previousPage;
    paginationRequest();
};

const assignPages = (page) => {
    document.getElementById("page-1").firstChild.textContent = page - 2;
    document.getElementById("page-2").firstChild.textContent = page - 1;
    document.getElementById("page-3").firstChild.textContent = page;
    document.getElementById("page-4").firstChild.textContent = page + 1;
    document.getElementById("page-5").firstChild.textContent = page + 2;
};


const addToHandler = (event) => {
    let elem = event.target;
    while (elem.tagName !== 'BUTTON') {
        elem = elem.parentElement;
    }
    elem = elem.parentElement;
    let product = {};
    let pattern = `\\/app\\/${entity}\\/view.*`;
    let regexp = new RegExp(pattern, 'g');
    if (!currentURL.match(regexp)) {

        product.id = Number.parseInt(elem.querySelector('input[name="id"]').value);
        let quantity = elem.querySelector('input[name="quantity"]').value;
        if (quantity !== '' && Number.parseInt(quantity) <= 0)
            return;

        quantity = quantity === '' ? 1 : Number.parseInt(quantity);

        $.ajax(`/app/products/${product.id}`, {method: 'GET'}).done((data) => {
            if (data.product.quantityOnStock < quantity) {
                allMessages = data.messages;
                let productUnavailableModal = $('#productUnavailable').get(0);
                let modalBody = productUnavailableModal.querySelector('.modal-body');
                modalBody.textContent = allMessages.productUnavailable;
                $(productUnavailableModal).modal('toggle');
                return;
            }

            if (check.products.some(p => p.id === product.id)) {
                check.products.forEach(pr => {
                    if (pr.id === product.id) {
                        pr.boughtQuantity += quantity;
                        pr.quantityOnStock -= quantity;
                        product = pr;
                    }
                });
            } else {
                let imgElem = elem.parentElement.querySelector('img');
                let imgSrc = imgElem.getAttribute('src');
                imgSrc = imgSrc.slice(imgSrc.indexOf(',') + 1);
                product.title = elem.querySelector('input[name="title"]').value;
                product.code = Number.parseInt(elem.querySelector('input[name="code"]').value);
                product.price = Number.parseFloat(elem.querySelector('input[name="price"]').value);
                product.image = imgSrc;
                product.quantityType = elem.querySelector('input[name="quantityType"]').value;
                product.boughtQuantity = quantity;
                product.quantityOnStock = Number.parseInt(elem.querySelector('input[name="quantityStock"]').value) - quantity;
                product.quantityOnStock = product.quantityOnStock - quantity;
                check.products.push(product);
            }

            if (check.products.length > 0) {
                check.sum = 0;
                check.products.forEach(p => {
                    check.sum += p.price * p.boughtQuantity;
                });
                check.sum = Math.round(check.sum * 100) / 100;
                let div = $('.container').get(0).children[0].lastElementChild;
                if (div.children.length > 0) {
                    div.firstElementChild.querySelector('span').textContent = `Sum: ${check.sum}`;
                } else {
                    let closeCheck = document.createElement('DIV');
                    closeCheck.classList.add('input-group');
                    closeCheck.insertAdjacentHTML('beforeEnd', `
                    <div class="input-group-prepend">
                        <span class="input-group-text">${allMessages.sum}: ${check.sum}</span>
                    </div>
                    <button class="btn btn-outline-success" style="border-bottom-left-radius: 0; 
                        border-top-left-radius: 0" type="button" id="close" name="close" 
                        onclick="closeCheckHandler()">${allMessages.closeCheck}</button>
                `);

                    div.appendChild(closeCheck);
                }
            }
        }).fail(function (jqXHR) {
            let html = document.getElementsByTagName('HTML')[0];
            html.innerHTML = jqXHR.responseText;
        });
    } else {
        if (entity === 'checks') {
            let prodInfo = elem.parentElement;
            let productPrice = prodInfo.querySelector('input[id="productPrice"]').value;
            let productId = prodInfo.querySelector('input[id="productId"]').value;

            let quantityModal = $('#quantityModal').get(0);
            quantityModal.querySelector('label[for="quantity"]').textContent = allMessages.quantityToAdd;
            $(quantityModal).off('hide.bs.modal').on('hide.bs.modal', (event) => {
                let enteredQuantity = quantityModal.querySelector('input[id="quantity"]').value;
                quantityModal.querySelector('input[id="quantity"]').value = '';
                if (enteredQuantity === '' || enteredQuantity <= 0)
                    return;

                let boughtQuantityTd = elem.parentElement.children[4];
                enteredQuantity = Number.parseInt(enteredQuantity);

                product = {
                    id: Number.parseInt(productId),
                    title: prodInfo.querySelector('input[id="productTitle"]').value,
                    code: Number.parseInt(prodInfo.querySelector('input[id="productCode"]').value),
                    price: Number.parseFloat(productPrice),
                    boughtQuantity: enteredQuantity,
                    quantityType: prodInfo.querySelector('input[id="productQuantityType"]').value,
                    quantityOnStock: 0,
                    image: prodInfo.querySelector('input[id="productImage"]').value
                };

                $.ajax(`/app/products/${product.id}`, {method: 'GET'}).done((data) => {
                    if (data.product.quantityOnStock < enteredQuantity) {
                        allMessages = data.messages;
                        let productUnavailableModal = $('#productUnavailable').get(0);
                        let modalBody = productUnavailableModal.querySelector('.modal-body');
                        modalBody.textContent = allMessages.productUnavailable;
                        $(productUnavailableModal).modal('toggle');
                        return;
                    }

                    let boughtNumber = enteredQuantity;
                    if (check.products.some(p => p.id === product.id)) {
                        check.products.forEach(pr => {
                            if (pr.id === product.id) {
                                pr.boughtQuantity += product.boughtQuantity;
                                product = pr;
                            }
                        });
                    } else {
                        check.products.push(product);
                    }

                    boughtQuantityTd.textContent = product.boughtQuantity + '';
                    let quantityOnStock = Number.parseInt(boughtQuantityTd.nextElementSibling.textContent);
                    quantityOnStock -= boughtNumber;
                    boughtQuantityTd.nextElementSibling.textContent = quantityOnStock + '';
                    product.quantityOnStock = quantityOnStock;

                    if (quantityOnStock === 0) {
                        elem.innerHTML = '';
                    }
                    if (elem.children.length === 1 || elem.children.length === 0) {
                        elem.insertAdjacentHTML('beforeEnd', `                            
                        <button class="btn btn-outline-danger ml-md-2" onclick="deleteFromHandler(event)">
                            <i class="fa fa-trash"></i> ${allMessages.delete}
                        </button>               
                    `);
                    }

                    let checkForm = $('.add-form-content').get(0);
                    check.sum = 0;
                    check.products.forEach(p => {
                        check.sum += p.price * p.boughtQuantity;
                    });
                    check.sum = Math.round(check.sum * 100) / 100;
                    checkForm.querySelector('input[id="sum"]').value = check.sum;
                }).fail(function (jqXHR) {
                    let html = document.getElementsByTagName('HTML')[0];
                    html.innerHTML = jqXHR.responseText;
                });
            });
            $(quantityModal).modal('toggle');
        } else if (entity === 'reports') {
            let checkInfo = elem.parentElement;
            let check = {
                id: Number.parseInt(checkInfo.querySelector('input[id="checkId"]').value),
                sum: Number.parseFloat(checkInfo.querySelector('input[id="checkSum"]').value)
            };

            if (report.checks.some(c => c.id === check.id)) {
                report.checks.forEach(ch => {
                    if (ch.id === check.id) {
                        check = ch;
                    }
                });
            } else {
                report.checks.push(check);
            }
            elem.innerHTML = '';
            elem.insertAdjacentHTML('beforeEnd', `                            
                 <button class="btn btn-outline-danger ml-md-2" onclick="deleteFromHandler(event)">
                   <i class="fa fa-trash"></i> ${allMessages.delete}
                 </button>               
            `);

            let reportForm = $('.add-form-content').get(0);
            report.totalSum = 0;
            report.checks.forEach(c => {
                report.totalSum += c.sum;
            });
            report.totalSum = Math.round(report.totalSum * 100) / 100;
            reportForm.querySelector('input[id="totalSum"]').value = report.totalSum;
        }
    }
};

const deleteFromHandler = (event) => {
    let elem = event.target;
    while (elem.tagName !== 'BUTTON') {
        elem = elem.parentElement;
    }
    elem = elem.parentElement;
    let product = {};
    if (currentURL.match(/.*home.*/g)) {

    } else {
        if (entity === 'checks') {
            let quantityModal = $('#quantityModal').get(0);
            quantityModal.querySelector('label[for="quantity"]').textContent = allMessages.quantityToRemove;
            $(quantityModal).off('hide.bs.modal').on('hide.bs.modal', (event) => {
                let enteredQuantity = quantityModal.querySelector('input[id="quantity"]').value;
                quantityModal.querySelector('input[id="quantity"]').value = '';
                if (enteredQuantity === '' || enteredQuantity <= 0)
                    return;

                let boughtQuantTd = elem.parentElement.children[4];
                let boughtQuantCurrent = Number.parseInt(boughtQuantTd.textContent);
                enteredQuantity = Number.parseInt(enteredQuantity);
                if (enteredQuantity > boughtQuantCurrent) {
                    enteredQuantity = boughtQuantCurrent;
                }

                let prodId = elem.parentElement.querySelector('input[id="productId"]').value;
                product = {
                    id: Number.parseInt(prodId)
                };
                let boughtProducts = 0;
                if (check.products.length > 0) {
                    let pIndex = -1;
                    check.products.forEach((p, i) => {
                        if (p.id === product.id) {
                            pIndex = i;
                            p.quantityOnStock += enteredQuantity;
                            p.boughtQuantity -= enteredQuantity;
                        }
                    });
                    if (check.products[pIndex].boughtQuantity < 0) {
                        check.products[pIndex].boughtQuantity = 0;
                        // check.products.splice(pIndex, 1);
                    } else {
                        boughtProducts = check.products[pIndex].boughtQuantity;
                    }
                }


                boughtQuantTd.textContent = boughtProducts + '';
                let numberOnStock = Number.parseInt(boughtQuantTd.nextElementSibling.textContent);
                numberOnStock += enteredQuantity;
                boughtQuantTd.nextElementSibling.textContent = numberOnStock + '';
                if (boughtProducts === 0) {
                    elem.innerHTML = '';
                    elem.insertAdjacentHTML('beforeEnd', `                            
                        <button class="btn btn-outline-success mb-md-1" onclick="addToHandler(event)">
                            <i class="fa fa-plus-circle"></i> ${allMessages.add}
                        </button>               
                    `);
                }
                let checkForm = $('.add-form-content').get(0);
                check.sum = 0;
                check.products.forEach(p => {
                    check.sum += p.price * p.boughtQuantity;
                });
                check.sum = Math.round(check.sum * 100) / 100;
                checkForm.querySelector('input[id="sum"]').value = check.sum;
            });
            $(quantityModal).modal('toggle');
        } else if (entity === 'reports') {
            let checkInfo = elem.parentElement;
            let check = {
                id: Number.parseInt(checkInfo.querySelector('input[id="checkId"]').value)
            };

            if (report.checks.length > 0) {
                let cIndex = -1;
                report.checks.forEach((ch, index) => {
                    if (ch.id === check.id) {
                        cIndex = index;
                    }
                });
                report.checks.splice(cIndex, 1);
            }
            elem.innerHTML = '';
            elem.insertAdjacentHTML('beforeEnd', `                            
                <button class="btn btn-outline-success ml-md-2" onclick="addToHandler(event)">
                    <i class="fa fa-plus-circle"></i> ${allMessages.add}
                </button>               
            `);

            let reportForm = $('.add-form-content').get(0);
            report.totalSum = 0;
            report.checks.forEach(c => {
                report.totalSum += c.sum;
            });
            report.totalSum = Math.round(report.totalSum * 100) / 100;
            reportForm.querySelector('input[id="totalSum"]').value = report.totalSum;
        }
    }
};

const editEntityHandler = (event) => {
    let formContent = document.getElementsByClassName('add-form-content')[0];
    let formContentCss = getComputedStyle(formContent);
    let itemTable = formContent.parentElement.nextElementSibling;
    if (formContentCss.display !== 'none') {
        $(formContent).slideUp('slow');
        $(itemTable).slideUp('slow');
        formContent.innerHTML = '';
        itemTable.innerHTML = '';

        if (entity === 'checks') {
            let cf = $('.container-fluid').get(0);
            cf.classList.remove('container-fluid');
            cf.classList.add('container');
        }

        return;
    }

    let input = event.target;
    while (input.tagName !== 'BUTTON')
        input = input.parentElement;

    input = input.parentElement.parentElement.lastElementChild;
    let uri;
    if (input.tagName !== 'INPUT')
        uri = `/app/${entity}/messages`;
    else
        uri = `/app/${entity}/${input.value}`;


    let xhr = new XMLHttpRequest();
    xhr.open('GET', uri, true);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== 4)
            return;

        let data = JSON.parse(xhr.response);
        allMessages = data.messages === undefined ? data : data.messages;
        if (entity === 'checks' && data.check === undefined)
            check = {products: []};
        else if (entity === 'checks' && data.check !== undefined)
            check = data.check;
        else if (entity === 'reports' && data.report === undefined)
            report = {checks: [], totalSum: 0};
        else if (entity === 'reports' && data.report !== undefined)
            report = data.report;

        if (data.messages === undefined) {
            data = {messages: data};
        }
        renderEntity(data, 'form');
        $(formContent).slideDown('slow');
    };
    xhr.send();

    saveHandler();
};

function saveHandler() {
    if ($('#save').length === 0) {
        setTimeout(saveHandler, 50);
        return;
    }

    $('#save').click((event) => {

        if (entity === 'checks')
            saveCheck(event);
        else if (entity === 'products')
            saveProduct(event);
        else if (entity === 'reports')
            saveReport(event);
        else if (entity === 'employees')
            saveEmployee(event);

    });
}

const deleteEntityHandler = (event) => {
    let trElem = event.target;
    while (trElem.tagName !== 'TR') {
        trElem = trElem.parentElement;
    }

    let entityId = trElem.querySelector('input[id="id"]').value;
    $.ajax(`/app/${entity}/${entityId}`, {method: 'DELETE'})
        .done((data, textStatus) => {
            if (textStatus === 'success') {
                window.location.href = `/app/${entity}/view`;
            }
        }).fail(function (jqXHR) {
        if (entity === 'checks' && jqXHR.status === 500) {
            let restrictionModal = $('#restriction').get(0);
            let mBody = restrictionModal.querySelector('.modal-body');
            mBody.textContent = allMessages.checkRestriction;
            $(restrictionModal).modal('toggle');
        } else {
            let html = document.getElementsByTagName('HTML')[0];
            html.innerHTML = jqXHR.responseText;
        }
    });
};

const closeCheckHandler = () => {
    check.date = getDate();
    check.status = 'CLOSED';
    let emp = $('.container').get(0).firstElementChild.firstElementChild;
    check.employee = {
        id: Number.parseInt(emp.querySelector('input[id="employeeId"]').value),
        firstName: $('#employeeFirstName').val(),
        lastName: $('#employeeLastName').val()
    };

    $.ajax(`/app/checks`, {
        method: 'POST',
        data: JSON.stringify(check),
        headers: {'Content-Type': 'application/json'}
    }).done((data) => {
        if (data.errors === undefined) {
            let modal = $('#checkClosed').get(0);
            modal.querySelector('.modal-body').textContent = allMessages.sum + ': ' + check.sum;
            $(modal).off('hidden.bs.modal').on('hidden.bs.modal',
                () => {
                    window.location.href = '/app/home'
                });
            $(modal).modal('toggle');
        } else {
            let productUnavailableModal = $('#productUnavailable').get(0);
            let modalBody = productUnavailableModal.querySelector('.modal-body');
            modalBody.textContent = data.errors.unavailableProducts;
            let unavailableProducts = data.unavailableProducts;
            let listElem = document.createElement('UL');
            for (let i = 0; i < unavailableProducts.length; i++) {
                listElem.insertAdjacentHTML('beforeEnd', `
                        <li>${unavailableProducts[i].title}: ${unavailableProducts[i].code}</li>
                    `);
            }
            modalBody.appendChild(listElem);
            $(productUnavailableModal).modal('toggle');
        }
    }).fail(function (jqXHR) {
        let html = document.getElementsByTagName('HTML')[0];
        html.innerHTML = jqXHR.responseText;
    });
};

function getDate() {
    let date = new Date();
    return formatForJava(date);
}

function parseJsonDate(date, forInput) {
    if (date == null || date === undefined)
        return '';

    let year = date.year;
    let month = date.monthValue;
    let day = date.dayOfMonth;
    let hours = date.hour;
    let minutes = date.minute;
    if (month < 10) {
        month = '0' + month;
    }
    if (day < 10) {
        day = '0' + day;
    }
    if (hours < 10) {
        hours = '0' + hours;
    }
    if (minutes < 10) {
        minutes = '0' + minutes;
    }
    if (forInput)
        return year + '-' + month + '-' + day + 'T' + hours + ':' + minutes;
    else
        return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes;
}

function formatForJava(date) {
    let day = date.getDate();
    let month = date.getMonth() + 1;
    let year = date.getFullYear();
    let hours = date.getHours();
    let minutes = date.getMinutes();
    if (day < 10) {
        day = '0' + day;
    }
    if (month < 10) {
        month = '0' + month;
    }
    if (hours < 10) {
        hours = '0' + hours;
    }
    if (minutes < 10) {
        minutes = '0' + minutes;
    }

    return month + '-' + day + '-' + year + ' ' + hours + ':' + minutes;
}

const renderEntity = (json, view) => {
    if (entity === 'products') {
        if (view === 'form') {
            productForm(json.product, json.messages, $('.add-form-content').get(0), json.errors);
        } else if (view === 'table') {
            let elem = document.getElementsByClassName('well')[0];
            if (elem.firstElementChild.tagName === 'TABLE')
                elem.removeChild(elem.firstElementChild);
            let product = json.product != null ? Array.of(json.product) : [];
            product = json.products != null ? json.products : product.length > 0 ? product : [];
            productsTable(product, json.messages, elem);
        } else if (view === 'card') {
            let elem = $('.container').get(1).children[0];
            for (let i = 0; i < 6; i++) {
                if (i === 3) {
                    elem = $('.container').get(2).children[0];
                }
                if (json.products[i].quantityOnStock <= 0) {
                    continue;
                }
                productCard(json.products[i], json.messages, elem);
            }
        }
    } else if (entity === 'checks') {
        if (view === 'form') {
            checkForm(json.check, json.messages, $('.add-form-content').get(0), json.errors);

            let div = $('.add-form').get(0).parentElement.nextElementSibling;
            div.style.display = 'none';
            let divTable = document.createElement('DIV');
            divTable.classList.add('well');
            divTable.style.maxHeight = '500px';
            divTable.style.overflowY = 'auto';
            div.insertAdjacentHTML('beforeEnd', `<div class="input-group">
                    <input id="productSearch" type="text" class="form-control" style="border-color: #a0b1ff"
                       onkeyup="productSearchHandler(event)" placeholder="${allMessages.productSearch}">
                </div>
            `);
            div.appendChild(divTable);
            let checksProducts = json.check === undefined ? [] : json.check.products;
            checkProductsTable(checksProducts, json.messages, divTable);
            let container = document.getElementsByClassName('container')[0];
            container.classList.remove('container');
            container.classList.add('container-fluid');
            let row = container.firstElementChild;
            row.firstElementChild.classList.remove('col-md-5');
            row.firstElementChild.classList.add('col-md-4');
            row.lastElementChild.classList.remove('col-md-7');
            row.lastElementChild.classList.add('col-md-8');
            setTimeout(() => {
                $(div).slideDown('slow')
            }, 100);
        } else if (view === 'table') {
            let elem = document.getElementsByClassName('main')[0];
            elem = elem.querySelector('.well');
            if (elem.firstElementChild.tagName === 'TABLE')
                elem.removeChild(elem.firstElementChild);
            let check = json.check != null ? Array.of(json.check) : [];
            check = json.checks != null ? json.checks : check.length > 0 ? check : [];
            checksTable(check, json.messages, elem);
        }
    } else if (entity === 'reports') {
        if (view === 'form') {
            reportForm(json.report, json.messages, $('.add-form-content').get(0), json.errors);

            let div = $('.add-form').get(0).parentElement.nextElementSibling;
            div.style.display = 'none';
            let divTable = document.createElement('DIV');
            divTable.classList.add('well');
            divTable.style.maxHeight = '500px';
            divTable.style.overflowY = 'auto';
            div.insertAdjacentHTML('beforeEnd', `<div class="input-group">
                <input id="checkSearch" type="text" class="form-control" style="border-color: #a0b1ff"
                       onkeyup="checkSearchHandler(event)" placeholder="${allMessages.checkSearch}">
                </div>
            `);
            div.appendChild(divTable);
            let reportChecks = json.report === undefined ? [] : json.report.checks;
            reportChecksTable(reportChecks, json.messages, divTable);
            let container = document.getElementsByClassName('container')[0];
            let row = container.firstElementChild;
            row.firstElementChild.classList.remove('col-md-5');
            row.firstElementChild.classList.add('col-md-4');
            row.lastElementChild.classList.remove('col-md-7');
            row.lastElementChild.classList.add('col-md-8');
            setTimeout(() => {
                $(div).slideDown('slow')
            }, 100);
        } else if (view === 'table') {
            let elem = document.getElementsByClassName('well')[0];
            if (elem.firstElementChild.tagName === 'TABLE')
                elem.removeChild(elem.firstElementChild);
            reportsTable(json.reports, json.messages, elem);
        }
    } else if (entity === 'employees') {
        if (view === 'table') {
            let elem = document.getElementsByClassName('well')[0];
            if (elem.firstElementChild.tagName === 'TABLE')
                elem.removeChild(elem.firstElementChild);
            employeesTable(json.employees, json.messages, elem);
        } else if (view === 'form') {
            employeeForm(json.employee, json.messages, $('.add-form-content').get(0), json.errors);
        }
    } else if (entity === 'users') {
        if (view === 'table') {
            let elem = document.getElementsByClassName('well')[0];
            if (elem.firstElementChild.tagName === 'TABLE')
                elem.removeChild(elem.firstElementChild);
            usersTable(json.users, json.messages, elem);
        }
    }
};

const productsTable = (products, messages, htmlElem) => {
    let table = document.createElement('table');
    table.classList.add('table', 'table-striped');
    let thead = document.createElement('thead');
    thead.insertAdjacentHTML('beforeEnd', `
        <tr>
            <th>${messages.title}</th>
            <th>${messages.code}</th>
            <th>${messages.price} USD</th>
            <th>${messages.quantityType}</th>
            <th>${messages.quantityOnStock}</th>
            <th>${messages.edit}</th>
            <th>${messages.delete}</th>
        </tr>
    `);

    let tbody = document.createElement('tbody');
    for (let i = 0; i < products.length; i++) {
        tbody.insertAdjacentHTML('beforeEnd', `
            <tr>
                <td>${products[i].title}</td>
                <td>${products[i].code}</td>
                <td>${products[i].price}</td>
                <td>${products[i].quantityType}</td>
                <td>${products[i].quantityOnStock}</td>
                <td>
                    <button class="btn btn-outline-info add-form">
                        <i class="fa fa-edit"></i> ${messages.edit}
                    </button>
                </td>
                <td>
                    <button class="btn btn-outline-danger delete">
                        <i class="fa fa-trash"></i> ${messages.delete}
                    </button>
                </td>
                <input type="hidden" id="id" name="id" value="${products[i].id}">
            </tr>
        `);
    }

    table.appendChild(thead);
    table.appendChild(tbody);

    htmlElem.insertBefore(table, htmlElem.children[0]);
};

const checkProductsTable = (products, messages, htmlElem) => {
    let table = document.createElement('table');
    table.classList.add('table', 'table-striped');
    let thead = document.createElement('thead');
    thead.insertAdjacentHTML('beforeEnd', `
        <tr>
            <th>${messages.title}</th>
            <th>${messages.code}</th>
            <th>${messages.price} USD</th>
            <th>${messages.quantityType}</th>
            <th>${messages.boughtQuantity}</th>
            <th>${messages.quantityOnStock}</th>           
        </tr>
    `);

    let tbody = document.createElement('tbody');
    for (let i = 0; i < products.length; i++) {
        let inCheck = check.products.some(p => p.id === products[i].id);
        let enoughQuantity = products[i].quantityOnStock > 0;
        if (!enoughQuantity && !inCheck)
            continue;

        let tr = document.createElement('TR');
        tr.insertAdjacentHTML('beforeEnd', `
                <td>${products[i].title}</td>
                <td>${products[i].code}</td>
                <td>${products[i].price}</td>
                <td>${products[i].quantityType}</td>                
        `);
        if (check.products.length !== 0) {
            let boughtQuant = 0;
            check.products.forEach(p => {
                if (p.id === products[i].id)
                    boughtQuant = p.boughtQuantity;
            });
            tr.insertAdjacentHTML('beforeEnd', `
                <td>${boughtQuant}</td>
            `);
        } else {
            tr.insertAdjacentHTML('beforeEnd', `
                <td>${products[i].boughtQuantity}</td>
            `);
        }
        let prodIndex = -1;
        if (check.products.length !== 0) {
            check.products.forEach((p, index) => {
                if (p.id === products[i].id)
                    prodIndex = index;
            });
        }
        tr.insertAdjacentHTML('beforeEnd', `
            <td>${prodIndex !== -1 ? check.products[prodIndex].quantityOnStock : products[i].quantityOnStock}</td>                
            <input type="hidden" id="productId" name="productId" value="${products[i].id}">
            <input type="hidden" id="productTitle" name="productTitle" value="${products[i].title}">
            <input type="hidden" id="productCode" name="productCode" value="${products[i].code}">
            <input type="hidden" id="productPrice" name="productPrice" value="${products[i].price}">
            <input type="hidden" id="productQuantityType" name="productQuantityType" value="${products[i].quantityType}">
            <input type="hidden" id="productImage" name="productImage" value="${products[i].image}">
        `);
        let tdButtons = document.createElement('TD');
        if (enoughQuantity) {
            tdButtons.insertAdjacentHTML('beforeEnd', `                              
                    <button class="btn btn-outline-success mb-md-1" onclick="addToHandler(event)">
                        <i class="fa fa-plus-circle"></i> ${messages.add}
                    </button>               
            `);
        }
        if (prodIndex !== -1 && check.products[prodIndex].boughtQuantity > 0) {
            tdButtons.insertAdjacentHTML('beforeEnd', `                               
                    <button class="btn btn-outline-danger ml-md-2" onclick="deleteFromHandler(event)">
                        <i class="fa fa-trash"></i> ${messages.delete}
                    </button>                
            `);
        }
        tr.appendChild(tdButtons);
        tbody.appendChild(tr);
    }

    table.appendChild(thead);
    table.appendChild(tbody);

    htmlElem.insertBefore(table, htmlElem.children[0]);
};

const productForm = (product, messages, htmlElem, errors) => {
    let card = document.createElement('div');
    card.classList.add('card');
    let cardHeader = document.createElement('div');
    cardHeader.classList.add('card-header');
    cardHeader.textContent = 'Item';
    let cardBody = document.createElement('div');
    cardBody.classList.add('card-body');

    let form = document.createElement('form');
    form.insertAdjacentHTML('beforeEnd', `
        <div class="alert alert-danger" style="display: ${errors !== undefined ? errors.codeExists !== undefined ? 'block' : 'none' : 'none'}">
            <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
            ${errors !== undefined ? errors.codeExists !== undefined ? errors.codeExists : '' : ''}      
        </div>
        <div class="form-group">
            <span style="color: red; display: ${errors !== undefined ? errors.title !== undefined ? 'inline' : 'none' : 'none'}">${errors !== undefined ? errors.title : ''}</span>
            <label for="title">${messages.title}</label>
            <input type="text" class="form-control" name="title" id="title" value="${product !== undefined ? product.title : ''}">
        </div>
        <div class="form-group">
            <span style="color: red; display: ${errors !== undefined ? errors.code !== undefined ? 'inline' : 'none' : 'none'}">${errors !== undefined ? errors.code : ''}</span>
            <label for="code">${messages.code}</label>
            <input type="number" class="form-control" name="code" id="code" value="${product !== undefined ? product.code : ''}">
        </div>
        <div class="form-group">
            <span style="color: red; display: ${errors !== undefined ? errors.price !== undefined ? 'inline' : 'none' : 'none'}">${errors !== undefined ? errors.price : ''}</span>
            <label for="price">${messages.price} USD</label>
            <input type="text" class="form-control" name="price" id="price" value="${product !== undefined ? product.price : ''}">
        </div>
        <div class="form-group">
            <label for="quantityType">${messages.quantityType}</label>
            <select id="quantityType" name="quantityType" class="form-control">
                <option value="PIECE" ${product !== undefined ? product.quantityType === 'PIECE' ? 'selected' : '' : 'seleted'}>piece</option>
                <option value="GRAM" ${product !== undefined ? product.quantityType === 'GRAM' ? 'selected' : '' : ''}>gram</option>
            </select>
        </div>
        <div class="form-group">
            <span style="color: red; display: ${errors !== undefined ? errors.quantityOnStock !== undefined ? 'inline' : 'none' : 'none'}">${errors !== undefined ? errors.quantityOnStock : ''}</span>
            <label for="quantityOnStock">${messages.quantityOnStock}</label>
            <input type="number" class="form-control" name="quantityOnStock" id="quantityOnStock" 
            value="${product !== undefined ? product.quantityOnStock : ''}">
        </div>
        <div class="form-group">
            <label for="image">${messages.image}</label>
            <input type="file" accept=".jpg" class="form-control" name="image" id="image">
        </div>
        <input id="id" type="hidden" value="${product !== undefined ? product.id > 0 ? product.id : '' : ''}">
        <input id="img" type="hidden" value="${product !== undefined ? product.image : ''}">
        <button id="save" type="button" class="btn btn-outline-success mt-md-3">${messages.save}</button>
    `);

    cardBody.appendChild(form);
    card.appendChild(cardHeader);
    card.appendChild(cardBody);

    htmlElem.appendChild(card);
};

const productCard = (product, messages, htmlElem) => {
    let col = document.createElement("div");
    col.classList.add('col-md-4');
    let card = document.createElement("div");
    card.classList.add('card');
    let cardHeader = document.createElement("div");
    cardHeader.classList.add('card-header');
    cardHeader.textContent = product.title + ' : ' + product.code;
    let cardBody = document.createElement("div");
    cardBody.classList.add('card-body');
    let cardFooter = document.createElement("div");
    cardFooter.classList.add('card-footer');

    cardBody.insertAdjacentHTML('beforeEnd', `
        <img src="data:image/jpg;base64,${product.image}" class="img-responsive" style="width: 100%"></div>
    `);

    cardFooter.insertAdjacentHTML('beforeEnd', `
        <form>
            <div class="form-row mb-md-1">
                <label class="col-sm-4" for="quantity">${messages.quantity}</label>
                <input type="number" class="form-control col-sm-8" id="quantity" name="quantity" placeholder="0">
            </div>
            <div class="form-row mb-md-2">
                <label class="col-sm-4" for="price">${messages.price} USD</label>
                <input type="number" class="form-control col-sm-8" id="price" name="price" value="${product.price}" disabled>
            </div>
            <div class="form-row">
                <label class="col-sm-4" for="quantityT">${messages.quantityType}</label>
                <input type="text" class="form-control col-sm-8" id="quantityT" name="quantityT" value="${product.quantityType}" disabled>
            </div>
        </form>
        <input type="hidden" name="id" id="id" value="${product.id}">
        <input type="hidden" name="title" id="title" value="${product.title}">
        <input type="hidden" name="code" id="code" value="${product.code}">
        <input type="hidden" name="quantityStock" id="quantityStock" value="${product.quantityOnStock}">
        <input type="hidden" name="quantityType" id="quantityType" value="${product.quantityType}">
        <button type="button" onclick="addToHandler(event)" class="btn btn-outline-primary mt-sm-3">
            ${messages.add}
        </button>
    `);

    card.appendChild(cardHeader);
    card.appendChild(cardBody);
    card.appendChild(cardFooter);
    col.appendChild(card);

    htmlElem.appendChild(col);
};

const checksTable = (checks, messages, htmlElem) => {
    let table = document.createElement('table');
    table.classList.add('table', 'table-striped');
    let thead = document.createElement('thead');
    thead.insertAdjacentHTML('beforeEnd', `
        <tr>
            <th>${messages.employee}</th>
            <th>${messages.sum}</th>
            <th>${messages.date}</th>
            <th>${messages.status}</th>
            <th>${messages.edit}</th>
            <th>${messages.delete}</th>
        </tr>
    `);

    let tbody = document.createElement('tbody');
    for (let i = 0; i < checks.length; i++) {
        tbody.insertAdjacentHTML('beforeEnd', `
            <tr>
                <td>${checks[i].employee.firstName} ${checks[i].employee.lastName}</td>
                <td>${checks[i].sum}</td>
                <td>${parseJsonDate(checks[i].date, false)}</td>
                <td>${checks[i].status}</td>
                <td>
                    <button class="btn btn-outline-info add-form">
                        <i class="fa fa-edit"></i> ${messages.edit}
                    </button>
                </td>
                <td>
                    <button class="btn btn-outline-danger delete">
                        <i class="fa fa-trash"></i> ${messages.delete}
                    </button>
                </td>
                <input type="hidden" id="id" name="id" value="${checks[i].id}">
            </tr>
        `);
    }

    table.appendChild(thead);
    table.appendChild(tbody);

    htmlElem.insertBefore(table, htmlElem.children[0]);
};

const checkForm = (check, messages, htmlElem, errors) => {
    let card = document.createElement('div');
    card.classList.add('card');
    let cardHeader = document.createElement('div');
    cardHeader.classList.add('card-header');
    cardHeader.textContent = 'Item';
    let cardBody = document.createElement('div');
    cardBody.classList.add('card-body');

    let form = document.createElement('form');
    if (errors !== undefined && errors.employeeNotFound !== undefined) {
        form.insertAdjacentHTML('beforeEnd', `
            <div class="alert alert-danger">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                <span style="display: ${errors.employeeNotFound !== undefined ? 'block' : 'none'}">
                    ${errors.employeeNotFound !== undefined ? errors.employeeNotFound : ''} 
                </span>                   
            </div>
        `);

    }
    form.insertAdjacentHTML('beforeEnd', `
        <div class="form-group">
            <label for="title">${messages.employee}</label>
            <input type="hidden" id="employee-id" name="employee-id" value="${check !== undefined ? check.employee.id : ''}">         
            <lable for="employee-fn">${messages.firstName}</lable>
            <input type="text" class="form-control mb-md-2 disabled" name="employee-fn" id="employee-fn" value="${check !== undefined ? check.employee.firstName : ''}" 
                ${check !== undefined ? check.id > 0 ? 'disabled' : '' : ''}>
            <lable for="employee-ln">${messages.lastName}</lable>
            <input type="text" class="form-control disabled" name="employee-ln" id="employee-ln" value="${check !== undefined ? check.employee.lastName : ''}" 
                ${check !== undefined ? check.id > 0 ? 'disabled' : '' : ''}>
        </div>
        <div class="form-group">
            <span style="color: red; display: ${errors !== undefined ? errors.sum !== undefined ? 'inline' : 'none' : 'none'}">
                ${errors !== undefined ? errors.sum !== undefined ? errors.sum : '' : ''}</span>
            <label for="sum">${messages.sum}</label>
            <input type="text" class="form-control" name="sum" id="sum" value="${check !== undefined ? check.sum : ''}">
        </div>
        <div class="form-group">
            <span style="color: red; display: ${errors !== undefined ? errors.date !== undefined ? 'inline' : 'none' : 'none'}">
                ${errors !== undefined ? errors.date !== undefined ? errors.date : '' : ''}</span>
            <label for="date">${messages.date}</label>
            <input type="datetime-local" class="form-control" name="date" id="date" value="${check !== undefined ? parseJsonDate(check.date, true) : ''}">
        </div>
        <div class="form-group">
            <label for="status">${messages.status}</label>
            <select id="status" name="status" class="form-control">
                <option value="CLOSED" ${check !== undefined ? check.status === 'CLOSED' ? 'selected' : '' : 'selected'}>closed</option>
                <option value="MODIFIED" ${check !== undefined ? check.status === 'MODIFIED' ? 'selected' : '' : ''}>modified</option>
                <option value="REFUNDED" ${check !== undefined ? check.status === 'REFUNDED' ? 'selected' : '' : ''}>refunded</option>
            </select>
        </div>   
        <input type="hidden" id="id" name="id" value="${check !== undefined ? check.id > 0 ? check.id : '' : ''}">   
        <button id="save" type="button" class="btn btn-outline-success mt-md-3">${messages.save}</button>
    `);

    cardBody.appendChild(form);
    card.appendChild(cardHeader);
    card.appendChild(cardBody);

    htmlElem.appendChild(card);
};

const reportsTable = (reports, messages, htmlElem) => {
    let table = document.createElement('table');
    table.classList.add('table', 'table-striped');
    let thead = document.createElement('thead');
    thead.insertAdjacentHTML('beforeEnd', `
        <tr>
            <th>${messages.sinceDate}</th>
            <th>${messages.untilDate}</th>
            <th>${messages.totalSum}</th>
            <th>${messages.creationDate}</th>
            <th>${messages.type}</th>
            <th>${messages.edit}</th>
            <th>${messages.delete}</th>
        </tr>
    `);

    let tbody = document.createElement('tbody');
    for (let i = 0; i < reports.length; i++) {
        tbody.insertAdjacentHTML('beforeEnd', `
            <tr>
                <td>${parseJsonDate(reports[i].since, false)}</td>
                <td>${parseJsonDate(reports[i].until, false)}</td>
                <td>${reports[i].totalSum}</td>
                <td>${parseJsonDate(reports[i].creationDate, false)}</td>
                <td>${reports[i].type}</td>
                <td>
                    <button class="btn btn-outline-info add-form">
                        <i class="fa fa-edit"></i> ${messages.edit}
                    </button>
                </td>
                <td>
                    <button class="btn btn-outline-danger delete">
                        <i class="fa fa-trash"></i> ${messages.delete}
                    </button>
                </td>
                <input type="hidden" id="id" name="id" value="${reports[i].id}">
            </tr>
        `);
    }

    table.appendChild(thead);
    table.appendChild(tbody);

    htmlElem.insertBefore(table, htmlElem.children[0]);
};

const reportForm = (report, messages, htmlElem, errors) => {
    let card = document.createElement('div');
    card.classList.add('card');
    let cardHeader = document.createElement('div');
    cardHeader.classList.add('card-header');
    cardHeader.textContent = 'Item';
    let cardBody = document.createElement('div');
    cardBody.classList.add('card-body');

    let form = document.createElement('form');
    form.insertAdjacentHTML('beforeEnd', `
        <div class="form-group">
            <span style="color: red; display: ${errors !== undefined ? 'inline' : 'none'}">${errors !== undefined ? errors.since : ''}</span>
            <label for="since-date">${messages.sinceDate}</label>
            <input type="datetime-local" class="form-control" name="since-date" id="since-date" value="${report !== undefined ? parseJsonDate(report.since, true) : ''}">
        </div>
        <div class="form-group">
            <span style="color: red; display: ${errors !== undefined ? 'inline' : 'none'}">${errors !== undefined ? errors.until : ''}</span>
            <label for="until-date">${messages.untilDate}</label>
            <input type="datetime-local" class="form-control" name="until-date" id="until-date" value="${report !== undefined ? parseJsonDate(report.until, true) : ''}">
        </div>
        <div class="form-group">
            <span style="color: red; display: ${errors !== undefined ? 'inline' : 'none'}">${errors !== undefined ? errors.totalSum : ''}</span>
            <label for="totalSum">${messages.totalSum}</label>
            <input type="text" class="form-control" name="totalSum" id="totalSum" value="${report !== undefined ? report.totalSum : ''}">
        </div>
        <div class="form-group">
            <span style="color: red; display: ${errors !== undefined ? 'inline' : 'none'}">${errors !== undefined ? errors.creationDate : ''}</span>
            <label for="creation-date">${messages.creationDate}</label>
            <input type="datetime-local" class="form-control" name="creation-date" id="creation-date" value="${report !== undefined ? parseJsonDate(report.creationDate, true) : ''}">
        </div>
        <div class="form-group">
            <label for="type">${messages.type}</label>
            <select id="type" name="type" class="form-control">
                <option value="CLOSED_CHECKS" ${report !== undefined ? report.status === 'CLOSED_CHECKS' ? 'selected' : '' : 'selected'}>closed checks</option>
                <option value="MODIFIED_CHECKS" ${report !== undefined ? report.status === 'MODIFIED_CHECKS' ? 'selected' : '' : ''}>modified checks</option>
                <option value="REFUNDED_CHECKS" ${report !== undefined ? report.status === 'REFUNDED_CHECKS' ? 'selected' : '' : ''}>refunded checks</option>
                <option value="MIXED_CHECKS" ${report !== undefined ? report.status === 'MIXED_CHECKS' ? 'selected' : '' : ''}>mixed checks</option>
            </select>
        </div>   
        <input type="hidden" id="id" name="id" value="${report !== undefined ? report.id > 0 ? report.id : '' : ''}">   
        <button id="save" type="button" class="btn btn-outline-success mt-md-3">${messages.save}</button>
    `);

    cardBody.appendChild(form);
    card.appendChild(cardHeader);
    card.appendChild(cardBody);

    htmlElem.appendChild(card);
};

const reportChecksTable = (checks, messages, htmlElem) => {
    let table = document.createElement('table');
    table.classList.add('table', 'table-striped');
    let thead = document.createElement('thead');
    thead.insertAdjacentHTML('beforeEnd', `
        <tr>
            <th>${messages.employee}</th>
            <th>${messages.sum}</th>
            <th>${messages.date}</th>
            <th>${messages.status}</th>                     
        </tr>
    `);

    let tbody = document.createElement('tbody');
    for (let i = 0; i < checks.length; i++) {
        let tr = document.createElement('TR');
        tr.insertAdjacentHTML('beforeEnd', `
            <td>${checks[i].employee.firstName} ${checks[i].employee.lastName}</td>
            <td>${checks[i].sum}</td>
            <td>${parseJsonDate(checks[i].date, false)}</td>
            <td>${checks[i].status}</td>      
            <input type="hidden" id="checkId" name="checkId" value="${checks[i].id}">
            <input type="hidden" id="checkEmp-id" name="CheckEmp-id" value="${checks[i].employee.id}">
            <input type="hidden" id="checkSum" name="checkSum" value="${checks[i].sum}">
            <input type="hidden" id="checkDate" name="checkDate" value="${parseJsonDate(checks[i].date, false)}">
            <input type="hidden" id="checkStatus" name="checkStatus" value="${checks[i].status}">          
        `);

        let tdButtons = document.createElement('TD');
        if (report.checks.some(c => c.id === checks[i].id)) {
            tdButtons.insertAdjacentHTML('beforeEnd', `                               
                    <button class="btn btn-outline-danger ml-md-2" onclick="deleteFromHandler(event)">
                        <i class="fa fa-trash"></i> ${messages.delete}
                    </button>                
            `);
        } else {
            tdButtons.insertAdjacentHTML('beforeEnd', `                              
                    <button class="btn btn-outline-success" onclick="addToHandler(event)">
                        <i class="fa fa-plus-circle"></i> ${messages.add}
                    </button>               
            `);
        }
        tr.appendChild(tdButtons);
        tbody.appendChild(tr);
    }

    table.appendChild(thead);
    table.appendChild(tbody);

    htmlElem.insertBefore(table, htmlElem.children[0]);
};

const employeesTable = (employees, messages, htmlElem) => {
    let table = document.createElement('table');
    table.classList.add('table', 'table-striped');
    let thead = document.createElement('thead');
    thead.insertAdjacentHTML('beforeEnd', `
        <tr>
            <th>${messages.firstName}</th>
            <th>${messages.lastName}</th>
            <th>${messages.email}</th>
            <th>${messages.salary}</th>
            <th>${messages.position}</th>
            <th>${messages.edit}</th>           
        </tr>
    `);

    let tbody = document.createElement('tbody');
    for (let i = 0; i < employees.length; i++) {
        tbody.insertAdjacentHTML('beforeEnd', `
            <tr>
                <td>${employees[i].firstName}</td>
                <td>${employees[i].lastName}</td>
                <td>${employees[i].email}</td>
                <td>${employees[i].salary}</td>
                <td>${employees[i].position}</td>
                <td>
                    <button class="btn btn-outline-info add-form">
                        <i class="fa fa-edit"></i> ${messages.edit}
                    </button>
                </td>              
                <input type="hidden" id="id" name="id" value="${employees[i].id}">
            </tr>
        `);
    }

    table.appendChild(thead);
    table.appendChild(tbody);

    htmlElem.insertBefore(table, htmlElem.children[0]);
};

const employeeForm = (employee, messages, htmlElem, errors) => {
    let card = document.createElement('div');
    card.classList.add('card');
    let cardHeader = document.createElement('div');
    cardHeader.classList.add('card-header');
    cardHeader.textContent = 'Item';
    let cardBody = document.createElement('div');
    cardBody.classList.add('card-body');

    let form = document.createElement('form');
    form.insertAdjacentHTML('beforeEnd', `
        <div class="form-group">
            <span style="color: red; display: ${errors !== undefined ? 'inline' : 'none'}">${errors !== undefined ? errors.firstName : ''}</span>
            <label for="firstName">${messages.firstName}</label>
            <input type="text" class="form-control" name="firstName" id="firstName" value="${employee !== undefined ? employee.firstName : ''}" disabled>
        </div>
        <div class="form-group">
            <span style="color: red; display: ${errors !== undefined ? 'inline' : 'none'}">${errors !== undefined ? errors.lastName : ''}</span>
            <label for="lastName">${messages.lastName}</label>
            <input type="text" class="form-control" name="lastName" id="lastName" value="${employee !== undefined ? employee.lastName : ''}" disabled>
        </div>
        <div class="form-group">
            <span style="color: red; display: ${errors !== undefined ? 'inline' : 'none'}">${errors !== undefined ? errors.email : ''}</span>
            <label for="email">${messages.email}</label>
            <input type="email" class="form-control" name="email" id="email" value="${employee !== undefined ? employee.email : ''}" disabled>
        </div>
        <div class="form-group">
            <span style="color: red; display: ${errors !== undefined ? 'inline' : 'none'}">${errors !== undefined ? errors.salary : ''}</span>
            <label for="salary">${messages.salary}</label>
            <input type="text" class="form-control" name="salary" id="salary" value="${employee !== undefined ? employee.salary : ''}">
        </div>
        <div class="form-group">
            <label for="position">${messages.position}</label>
            <select name="position" id="position" class="form-control">
                <option value="CASHIER" ${employee !== undefined ? employee.position === 'CASHIER' ? 'selected' : '' : 'selected'}>cashier</option>
                <option value="SENIOR_CASHIER" ${employee !== undefined ? employee.position === 'SENIOR_CASHIER' ? 'selected' : '' : ''}>senior cashier</option>
                <option value="COMMODITIES_EXPERT" ${employee !== undefined ? employee.position === 'COMMODITIES_EXPERT' ? 'selected' : '' : ''}>commodities expert</option>
                <option value="ADMINISTRATOR" ${employee !== undefined ? employee.status === 'ADMINISTRATOR' ? 'selected' : '' : ''}>administrator</option>
            </select>
        </div>   
        <input type="hidden" id="id" name="id" value="${employee !== undefined ? employee.id > 0 ? employee.id : '' : ''}">   
        <button id="save" type="button" class="btn btn-outline-success mt-md-3">${messages.save}</button>
    `);

    cardBody.appendChild(form);
    card.appendChild(cardHeader);
    card.appendChild(cardBody);

    htmlElem.appendChild(card);
};

const usersTable = (users, messages, htmlElem) => {
    let table = document.createElement('table');
    table.classList.add('table', 'table-striped');
    let thead = document.createElement('thead');
    thead.insertAdjacentHTML('beforeEnd', `
        <tr>
            <th>${messages.login}</th>
            <th>${messages.role}</th>
            <th>${messages.employee}</th>                       
            <th>${messages.delete}</th>
        </tr>
    `);

    let tbody = document.createElement('tbody');
    for (let i = 0; i < users.length; i++) {
        tbody.insertAdjacentHTML('beforeEnd', `
            <tr>
                <td>${users[i].login}</td>
                <td>${users[i].role}</td>
                <td>${users[i].employee.firstName} ${users[i].employee.lastName}</td>                               
                <td>
                    <button class="btn btn-outline-danger delete">
                        <i class="fa fa-trash"></i> ${messages.delete}
                    </button>
                </td>
                <input type="hidden" id="id" name="id" value="${users[i].id}">
            </tr>
        `);
    }

    table.appendChild(thead);
    table.appendChild(tbody);

    htmlElem.insertBefore(table, htmlElem.children[0]);
};