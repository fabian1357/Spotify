package dhbw.service;

import java.io.IOException;
import java.lang.invoke.WrongMethodTypeException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dhbw.spotify.WrongRequestTypeException;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import dhbw.pojo.detail.album.DetailsAlbum;
import dhbw.pojo.detail.artist.DetailsArtist;
import dhbw.pojo.detail.track.DetailsTrack;
import dhbw.pojo.result.detail.DetailResult;
import dhbw.pojo.result.search.SearchResult;
import dhbw.pojo.result.search.SearchResultList;
import dhbw.pojo.search.album.Artist;
import dhbw.pojo.search.album.SearchAlbum;
import dhbw.pojo.search.artist.SearchArtist;
import dhbw.pojo.search.track.SearchTrack;
import dhbw.spotify.RequestCategory;
import dhbw.spotify.RequestType;
import dhbw.spotify.SpotifyRequest;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dhbw.spotify.RequestCategory.ARTIST;

@RestController
@RequestMapping("/service")
public class controller {

    @RequestMapping("/search")
    public SearchResult search(@RequestParam("type") RequestCategory type, @RequestParam("query") String query) throws IOException, WrongRequestTypeException {

        SpotifyRequest request = new SpotifyRequest(RequestType.SEARCH);
        SearchResult result = new SearchResult();
        ObjectMapper mapper = new ObjectMapper();
        List<SearchResultList> list = new ArrayList<>();
        Optional<String> optional = request.performeRequestSearch(type, query);

        if (optional.isPresent()) {
            String json = optional.get();
            switch (type) {
                case TRACK:
                    SearchTrack searchTrack = mapper.readValue(json, SearchTrack.class);
                    searchTrack.getTracks().getItems().forEach(t -> {
                        SearchResultList searchList = new SearchResultList();
                        searchList.setId(t.getId());
                        searchList.setTitle(t.getName());
                        searchList.setDescription(t.getType());
                        searchList.setPlayLink(t.getUri());
                        list.add(searchList);
                    });
                    break;
                case ALBUM:
                    SearchAlbum searchAlbum = mapper.readValue(json, SearchAlbum.class);
                    searchAlbum.getAlbums().getItems().forEach(al -> {
                        SearchResultList searchList = new SearchResultList();
                        searchList.setId(al.getId());
                        searchList.setTitle(al.getName());
                        searchList.setDescription(al.getType());
                        searchList.setPlayLink(al.getUri());
                        list.add(searchList);
                    });
                    break;
                case ARTIST:
                    SearchArtist searchArtist = mapper.readValue(json, SearchArtist.class);
                    searchArtist.getArtists().getItems().forEach(ar -> {
                        SearchResultList searchList = new SearchResultList();
                        searchList.setId(ar.getId());
                        searchList.setTitle(ar.getName());
                        searchList.setDescription(ar.getType());
                        searchList.setPlayLink(ar.getUri());
                        list.add(searchList);
                    });
                    break;
            }
        }
        result.setResults(list);
        return result;
    }

    @RequestMapping("/detail/{id}")
    public DetailResult detail(@RequestParam("type") RequestCategory type, @PathVariable("id") String id) throws IOException, WrongRequestTypeException {

        SpotifyRequest request = new SpotifyRequest(RequestType.DETAIL);
        DetailResult result = new DetailResult();
        ObjectMapper mapper = new ObjectMapper();
        Optional<String> optional = request.performeRequestDetail(type, id);

        if (optional.isPresent()) {
            String json = optional.get();
            switch (type) {
                case TRACK:
                    DetailsTrack detailsTrack = mapper.readValue(json, DetailsTrack.class);
                    result.setInfo(detailsTrack.getType());
                    result.setTitle(detailsTrack.getName());
                    break;
                case ALBUM:
                    DetailsAlbum detailsAlbum = mapper.readValue(json, DetailsAlbum.class);
                    result.setInfo(detailsAlbum.getType());
                    result.setTitle(detailsAlbum.getName());
                    break;
                case ARTIST:
                    DetailsArtist detailsArtist = mapper.readValue(json, DetailsArtist.class);
                    result.setInfo(detailsArtist.getType());
                    result.setTitle(detailsArtist.getName());
                    break;
            }
        }
        return result;
    }
}



