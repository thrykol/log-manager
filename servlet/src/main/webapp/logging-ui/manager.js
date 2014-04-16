var manager = {};

$(document).ready(function() {
	manager.get();

	$("form").on("submit", manager.submit)
		.find("input[type='button']")
			.on("click", manager.get);

});

manager.get = function() {
	$.get("manage", function(json) {
		if (json.status === "success") {
			$("#manager-logger > span:first").text(json.implementation)
			manager.tree(json.levels, json.loggers);
		}
	})
}

manager.submit = function() {
	var parameters = {};

	$(".pending").each(function() {
		var name = $(this).parents("li:first").data("name");
		parameters[name] = this.textContent;
	});

	$.post("manage", parameters, function() {
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
		var id = logger.replace(/[\.\$]/g, "-");
		var data = $("<span><span></span> - <code></code></span>")
			.first()
				.children("span")
					.text(logger)
				.end()
				.children("code")
					.text(loggers[logger].level)
				.end()
			.end();
		
		if(loggers[logger].isEffective) {
			data.find("code").addClass("effective");
		}
		
		loggerIds.push(id);
		cache[logger] = {
			data : "<span>" + data.html() + "</span>", // html returns the children
			attr : {
				id : id
			},
			metadata : {
				name : logger,
				original : loggers[logger].level,
				effective : loggers[logger].isEffective
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
			logger : logger,
			id : id,
			level : loggers[logger].level,
			effective : loggers[logger].isEffective
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
			manager.balloon(popup[i].logger, popup[i].id,
					popup[i].level, popup[i].effective, levels);
		}
	});

}

manager.balloon = function(logger, loggerId, level, isEffective, levels) {
	var options = $("<span></span>");
	for ( var i = 0; i < levels.length; i++) {
		var option = $("<input type='radio' onclick='manager.select($(this))'/><label></label>")
			.first()
				.attr("id", loggerId + "-" + levels[i])
				.attr("name", logger).val(levels[i])
				.prop("checked", levels[i] === level)
				.prop("loggerId", loggerId)
			.end()
			.last()
				.attr("for", loggerId + "-" + levels[i]).text(levels[i] + (levels[i] === level ? "*" : "")).end();

		options.append(option);
	}
	
	$("#" + loggerId + " > a:first > span:first").balloon({
		contents : options,
		position : "right",
		minLifetime : 0
	});
}

manager.select = function(elem) {
	var loggerId = elem.prop("loggerId");
	var logger = $("#" + loggerId);
	var original = logger.data("original");
	var effective = logger.data("effective");
	
	if(elem.val() === original) {
		var code = $("a:first > span:first > code", logger).removeClass("pending").text(elem.val());
		if(effective) {
			code.addClass("effective");
		}
	} else {
		$("a:first > span:first > code", logger).addClass("pending").removeClass("effective").text(elem.val());
	}
}
