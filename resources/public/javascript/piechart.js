function buildInputFrom(inputMap) {
    return Object.keys(inputMap).map(function (key) {
        return {text: key, value: inputMap[key]};
    });
}

function printPieChart(input, colors, targetDiv) {

    var width = 385,
        height = 200,
        radius = Math.min(width, height) / 2;

    var color = d3.scale.ordinal()
        .range(colors);

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

function hexVal(aval) {
    var hex = aval.toString(16);
    return hex.length == 1 ? "0" + hex : hex;
}

function gray(aval) {
    return "#" + hexVal(aval) + hexVal(aval) + hexVal(aval);
}

function getGrayScaleColors(numColors){
    var grayScale = [];
    var step = parseInt(230 / numColors);
    for(var i = 250; i > 20;i-=step){
        grayScale.push(gray(i));
    }
    return grayScale;
}

$(function () {
    var allPieCharts = d3.selectAll('div.piechart').each(function(d) {
        var currentChart = d3.select(this);
        var currentChartData = JSON.parse(currentChart.attr('data-piechart'));
        var currentChartColors = JSON.parse(currentChart.attr('data-piechart-colors'));
        if(!currentChartColors){
            currentChartColors = getGrayScaleColors(currentChartData.length);
        }
        printPieChart(currentChartData, currentChartColors, currentChart);
    })
});
