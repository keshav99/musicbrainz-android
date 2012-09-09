package org.musicbrainz.mobile.loader;

import java.io.IOException;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.webservice.MusicBrainzWebClient;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.loader.result.AsyncResult;
import org.musicbrainz.mobile.loader.result.LoaderStatus;
import org.musicbrainz.mobile.loader.result.SearchResults;
import org.musicbrainz.mobile.loader.result.SearchResults.SearchType;

public class SearchReleaseGroupLoader extends PersistingAsyncTaskLoader<AsyncResult<SearchResults>> {

    private String term;

    public SearchReleaseGroupLoader(String term) {
        this.term = term;
    }

    @Override
    public AsyncResult<SearchResults> loadInBackground() {
        try {
            MusicBrainz client = new MusicBrainzWebClient(App.getUserAgent());
            SearchResults results = new SearchResults(SearchType.RELEASE_GROUP, client.searchReleaseGroup(term));
            data = new AsyncResult<SearchResults>(LoaderStatus.SUCCESS, results);
            return data;
        } catch (IOException e) {
            return new AsyncResult<SearchResults>(LoaderStatus.EXCEPTION, e);
        }
    }
}
