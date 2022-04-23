/*

Copyright (C) 2013 David Dupplaw

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


*/
/**
 * 	A table with a set number of rows which rolls around as
 * 	data is displayed on it
 * 
 * 	@author David Dupplaw <dpd@ecs.soton.ac.uk>
 *	@created December 2010
 * 	@updated 30th October 2013 - Updated to latest jQuery widget factory
 *	@version 1.0
 */
(function($){

var RollingTableWidget = 
{
	// From http://snipplr.com/view.php?codeview&id=19601
	/** Fades one colour to another */
	_colourFade: function( minColor, maxColor, maxDepth, depth )
	{
	    function d2h(d) {return d.toString(16);}
	    function h2d(h) {return parseInt(h,16);}
	   
	    if(depth == 0)
	    {
	        return minColor;
	    }
	    
	    if(depth == maxDepth)
	    {
	        return maxColor;
	    }
	   
	    var color = "#";
	   
	    for(var i=1; i <= 6; i+=2){
	        var minVal = new Number(h2d(minColor.substr(i,2)));
	        var maxVal = new Number(h2d(maxColor.substr(i,2)));
	        var nVal = minVal + (maxVal-minVal) * (depth/maxDepth);
	        var val = d2h(Math.floor(nVal));
	        while(val.length < 2){
	            val = "0"+val;
	        }
	        color += val;
	    }
	    return color;
	},
	
	_nextRow: 0,
	
	/** Get the next row to be added to the table */
	getNextRow: function() { return this._nextRow; },
	
	/** Set the next row to be added to the table */
	setNextRow: function(r) { this._nextRow = r; },
	
	/** Get the maximum number of rows to show in the table */
	getMaxRowCount: function() { return this.options.maxRowCount; },

	/** Add an item into the table */
	addItem: function( data )
	{
		var row = this.getNextRow();

		// Rolling table, so we must remove the oldest 
		// row, if one exists
		$('#'+this.element.attr("id")+'_rollingtable_row_'+row).remove();
		
		var c = "a";
		if( row % 2 )
			c = "b"; 
		
		var tr = $("<tr id='"+this.element.attr("id")+"_rollingtable_row_"+row+"' "+
			"class='rollingtablerow_"+c+"'>");
		var ccc = 1;
		for( d in data )
		{
			if( typeof(data[d]) == "object" )
			{
				var td = $("<td class='col"+ccc+"'></td>" );
				td.append( data[d] );
				tr.append(td);
			}
			else
				tr.append( "<td class='col"+ccc+"'>"+data[d]+"</td>" );
			ccc++;
		}
		var addFromTop = this.options.addFromTop;
		if( addFromTop )
				$('#'+this.element.attr("id")+"_rollingtable tbody" ).prepend(tr);
		else	$('#'+this.element.attr("id")+"_rollingtable tbody" ).append(tr);
		tr.hide().fadeIn();
		
		this.setNextRow( (row+1)%this.getMaxRowCount() );
		
		// Now colour the rows
		var c1 = this.options.textColourTop;
		var c2 = this.options.textColourBottom;
		var max = this.getMaxRowCount();
		var x = max-$('#'+this.element.attr("id")+'_rollingtable tr').length;
		if( addFromTop )
			x = max;
		var me = this;
		$('#'+this.element.attr("id")+'_rollingtable tr').each( function(){
			$(this).children('td').css( 'color', me._colourFade(c1, c2, max-1, x) );
			if( addFromTop )
					x--;
			else	x++;
		});
	},

	_create: function()
	{
		var div = $('<div></div>');
		this.element.append( div );
		
		var caption = $("<p id='"+this.element.attr("id")+"_caption' class='caption'>"+this.options.title+"</p>");
		
		if( this.options.titlePosition == "above" && this.options.title != "" )
			div.append( caption );
		
		// Add a table that will show our data
		var tbl = $("<table id='"+this.element.attr("id")+"_rollingtable' class='rollingtable'>"+
			"<thead class='rollingtableheader'><tr></tr></thead>"+
			"<tbody class='rollingtablebody'></tbody></table>");
		div.append( tbl );	

		var columns = this.options.columns;
		for( c in columns )
		{
			$("#"+tbl.attr("id")+" thead tr").append( "<th>"+columns[c]+"</th>" );
		}
		
		tbl.width( this.element.width() );

		if( this.options.titlePosition == "below" && this.options.title != "" )
			div.append( caption );
	},
	
	options:
	{
		// Which is the next row to show
		nextRow: 1,
		
		// Default set of column names
		columns: ["Col 1","Col 2","Col 3"],
		
		// Maximum number of rows to show in the table
		maxRowCount: 10,
		
		// The caption to use for the table
		title: "",
		
		// Whether the caption is shown above or below the table
		titlePosition: "below",
		
		// The colour of the newest data
		textColourTop: "#666666",
		
		// The colour of the oldest data
		textColourBottom: '#FFFFFF',
		
		// Whether to add items to the top of the list or the bottom
		addFromTop: true
	}
}

$.widget( "dd.rollingtable", RollingTableWidget );
}(jQuery));