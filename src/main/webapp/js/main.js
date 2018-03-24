$(document).ready(() => {
    $('#searchSubmit').on('click', () => {
        document.getElementById('section').style.visibility = 'visible';
        const urlSearch = 'http://localhost:8080/service/search';
        const urlDetails = 'http://localhost:8080/service/detail/';

        var searchTerm = $('#searchTerm').val();
        var searchType = $('#searchType').val();
        var urlSearchThis = urlSearch + '?query=' + searchTerm + '&type=' + searchType;
        console.log('url:' + urlSearchThis);

        $('.progress').removeClass('hide');

        //clear table
        $('#resultList').empty();
        $('.searchFor').text(searchTerm);

        $.getJSON(urlSearchThis, (response) => {
            $('#resultList').on('click', '#infoButton', event=> {
                var spotifyId = $(event.currentTarget).data('spotifyid');
                var typeId = $(event.currentTarget).data('typeid');
                var urlDetailsThis = urlDetails + spotifyId + '?type=' + typeId;
                console.log('url:' + urlDetailsThis);

                $.getJSON(urlDetailsThis, (responseDetail) => {
                    var track = "track";
                    var album = "album";
                    var artist = "artist";

                    if (track == responseDetail.info) {
                        swal ({
                            title: "Weitere Infos zum Lied:",
                            text: ("Der Name des Liedes lautet: " + responseDetail.title),
                        })
                    } else {
                        if (responseDetail.info === artist) {
                            swal ({
                                title: "Weitere Infos zum Sänger:",
                                text: ("Der Name des Sängers lautet: " + responseDetail.title),
                            })
                        } else {
                            swal ({
                                title: "Weitere Infos zum Album:",
                                text: ("Der Name des Albums lautet: " + responseDetail.title),
                            })
                        }
                    }
                })
            });

            $('#resultList').on('click', '#playButton', event=> {
                $('#spotify-player').attr("src", "https://open.spotify.com/embed?uri=" + $(event.currentTarget).data('spotifylink'));
            });

            //append row for each item in response array
            $('#resultList').append(
                $.map(response.results, (item) => {
                    document.getElementById('table').style.visibility = 'visible';
            	    return '<tr>'
            	        + '<td>'
                	    + item.title
                	    + '</td><td>'
                	    + item.description
            	        + '</td><td>'
                	    + '<a class="waves-effect waves-light btn" data-typeid="' + searchType + '"data-spotifyid="' + item.id + '" id="infoButton"><i class="material-icons left">info</i>more</a>&nbsp;'
            	        + '<a class="waves-effect waves-light btn" data-spotifylink="' + item.playLink + '" id="playButton"><i class="material-icons left">play_circle_filled</i>play</a>'
            	        + '</td></tr>';
            }).join());
        }).fail((e) => {
            swal({
                 title: "An error occured!",
                 text: "Bitte gib einen Suchbegriff ein!",
                 type: "error"
            })
            console.log(e);
        }).always(() => {
            $('.progress').addClass('hide');
        });
    });
})