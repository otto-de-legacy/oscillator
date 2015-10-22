function buildInputFrom(inputMap) {
    return Object.keys(inputMap).map(function (key) {
        return {text: key, value: inputMap[key]};
    });
}

function printPieChart(input, targetDiv) {

    var width = 385,
        height = 200,
        radius = Math.min(width, height) / 2;

    var color = d3.scale.ordinal()
        .range(["#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#ff8c00"]);

    var arc = d3.svg.arc()
        .outerRadius(radius - 10)
        .innerRadius(0);

    var pie = d3.layout.pie()
        .sort(null)
        .value(function (d) {
            return d.value;
        });

    var svg = targetDiv.append("svg")
        .attr("width", width)
        .attr("height", height)
        .append("g")
        .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");


    var g = svg.selectAll(".arc")
        .data(pie(input))
        .enter().append("g")
        .attr("class", "arc");

    g.append("path")
        .attr("d", arc)
        .style("fill", function (d) {
            return color(d.data.value);
        });

    g.append("text")
        .attr("transform", function (d) {
            return "translate(" + arc.centroid(d) + ")";
        })
        .attr("dy", ".35em")
        .style("text-anchor", "middle")
        .text(function (d) {
            return d.data.text;
        });

}

$(function () {
    var allPieCharts = d3.selectAll('div.piechart').each(function(d) {
        var currentChart = d3.select(this);
        var currentChartDara = JSON.parse(currentChart.attr('data-piechart'));
        printPieChart(currentChartDara, currentChart);
    })
});
