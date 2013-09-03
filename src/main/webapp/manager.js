var manager = {};

manager.get = function() {
	$.get("api/manager", function(json) {
		if (json.status === "success") {
			$("#manager-logger > span:first").text(json.implementation)
			manager.tree(json.levels, json.loggers);
		}
	})
}

manager.submit = function() {
	var parameters = {};

	$("form > table > tbody > tr input:checked").each(function() {
		var input = $(this);
		var active = input.parents("tr:first").prop("active");

		if (active !== input.val()) {
			parameters[input.attr("name")] = input.attr("value");
		}
	})

	$.post("api/manager", parameters, function() {
		manager.get();
	});

	return false;
}

manager.tree = function(levels, loggers) {
	var cache = {};
	var loggerIds = [];
	var popup = [];

	var jsonData = [];
	for (logger in loggers) {
		var id = logger.replace(/\./g, "-");
		loggerIds.push(id);
		cache[logger] = {
			data : "<span>" + logger + " <code>" + loggers[logger]
					+ "</code></span>",
			attr : {
				id : id
			},
			metadata : {
				name : logger,
				original : loggers[logger]
			},
			children : []
		};

		var parts = logger.split("\.");
		if (parts.length === 1) {
			jsonData.push(cache[logger]);
		} else {
			var parent = cache[logger.substring(0, logger.lastIndexOf("\."))];
			parent.children.push(cache[logger]);
		}

		popup.push({
			id : id,
			level : loggers[logger]
		});
	}

	$("#manager-tree").jstree({
		"json_data" : {
			"data" : jsonData
		},
		core : {
			initially_open : loggerIds,
			html_titles : true
		},
		themes : {
			dots : true,
			icons : true
		},
		"plugins" : [ "themes", "json_data", "ui" ]
	}).bind("loaded.jstree", function(event, data) {
		for ( var i = 0; i < popup.length; i++) {
			manager.balloon(popup[i].id, popup[i].level, levels);
		}
	});

}

manager.balloon = function(loggerId, level, levels) {
	$("#" + loggerId + " > a:first > span:first").balloon({
		contents : "Logger: " + loggerId,
		position : "right",
		minLifetime : 0
	});
}