package org.musicbrainz.mobile.loader;

import java.io.IOException;

import org.musicbrainz.android.api.MusicBrainz;
import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.android.api.data.UserData;
import org.musicbrainz.android.api.webservice.Entity;
import org.musicbrainz.android.api.webservice.MusicBrainzWebClient;
import org.musicbrainz.mobile.App;
import org.musicbrainz.mobile.loader.result.AsyncEntityResult;
import org.musicbrainz.mobile.loader.result.LoaderStatus;

public class ReleaseLoader extends PersistingAsyncTaskLoader<AsyncEntityResult<Release>> {

    private String mbid;

    public ReleaseLoader(String mbid) {
        this.mbid = mbid;
    }

    @Override
    public AsyncEntityResult<Release> loadInBackground() {
        try {
            return getAvailableData();
        } catch (Exception e) {
            return new AsyncEntityResult<Release>(LoaderStatus.EXCEPTION, e);
        }
    }

    private AsyncEntityResult<Release> getAvailableData() throws IOException {
        if (App.isUserLoggedIn()) {
            return getReleaseWithUserData();
        } else {
            return getRelease();
        }
    }

    private AsyncEntityResult<Release> getRelease() throws IOException {
        MusicBrainz client = new MusicBrainzWebClient(App.getUserAgent());
        data = new AsyncEntityResult<Release>(LoaderStatus.SUCCESS, client.lookupRelease(mbid));
        return data;
    }

    private AsyncEntityResult<Release> getReleaseWithUserData() throws IOException {
        MusicBrainz client = new MusicBrainzWebClient(App.getCredentials());
        Release release = client.lookupRelease(mbid);
        UserData userData = client.lookupUserData(Entity.RELEASE_GROUP, release.getReleaseGroupMbid());
        data = new AsyncEntityResult<Release>(LoaderStatus.SUCCESS, release, userData);
        return data;
    }

}
