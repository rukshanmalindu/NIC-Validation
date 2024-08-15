document.addEventListener('DOMContentLoaded', function() {
    const uploadForm = document.getElementById('uploadForm');
    const fileInputs = [
        document.getElementById('fileInput1'),
        document.getElementById('fileInput2'),
        document.getElementById('fileInput3'),
        document.getElementById('fileInput4')
    ];
    const uploadStatus = document.getElementById('uploadStatus');
    const nicRecordsTable = document.getElementById('nicRecordsTable');

    uploadForm.addEventListener('submit', function(event) {
        event.preventDefault();

        const formData = new FormData();
        let filesAdded = 0;

        fileInputs.forEach((input, index) => {
            const file = input.files[0];
            if (file) {
                formData.append('files', file);
                filesAdded++;
            }
        });

        if (filesAdded < 4) {
            alert('Please select all four CSV files.');
            return;
        }

        fetch('http://localhost:8080/api/manage/upload-csv', {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(data => {
                if (data.message === "Files uploaded and processed successfully") {
                    uploadStatus.textContent = data.message;
                    loadNICRecords();
                } else {
                    uploadStatus.textContent = data.message || 'Error uploading files. Please try again.';
                }
            })
            .catch(error => {
                uploadStatus.textContent = 'Error: ' + error.message;
            });
    });

    function loadNICRecords() {
        fetch('http://localhost:8080/api/manage/api/nics')
            .then(response => response.json())
            .then(data => {
                nicRecordsTable.innerHTML = '';
                data.forEach(record => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${record.nicNumber}</td>
                        <td>${record.dob}</td>
                        <td>${record.age}</td>
                        <td>${record.gender}</td>
                    `;
                    nicRecordsTable.appendChild(row);
                });
            });
    }

    // Load NIC records on page load
    loadNICRecords();
});

document.addEventListener('DOMContentLoaded', function() {
    // Fetch summary data and update the DOM
    fetch('http://localhost:8080/api/manage/api/nicSummary')
        .then(response => response.json())
        .then(data => {S
            document.getElementById('totalRecords').textContent = data.totalRecords;
            document.getElementById('maleUsers').textContent = data.maleUsers;
            document.getElementById('femaleUsers').textContent = data.femaleUsers;

            // Generate the charts
            generateCharts(data.maleUsers, data.femaleUsers);
        });

    // Generate Bar and Pie charts
    function generateCharts(maleUsers, femaleUsers) {
        const barChart = new Chart(document.getElementById('barChart'), {
            type: 'bar',
            data: {
                labels: ['Male Users', 'Female Users'],
                datasets: [{
                    label: 'User Count',
                    data: [maleUsers, femaleUsers],
                    backgroundColor: ['#007bff', '#dc3545'],
                }]
            }
        });

        const pieChart = new Chart(document.getElementById('pieChart'), {
            type: 'pie',
            data: {
                labels: ['Male Users', 'Female Users'],
                datasets: [{
                    label: 'User Count',
                    data: [maleUsers, femaleUsers],
                    backgroundColor: ['#007bff', '#dc3545'],
                }]
            }
        });
    }


    function downloadReport(format) {
        let url = `http://localhost:8080/api/manage/download/${format}`;
        window.open(url, '_blank');
    }


});
