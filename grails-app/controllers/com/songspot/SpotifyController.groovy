package com.songspot

class SpotifyController {
    def utilitiesService
    def spotifyService

    def authorize() {
        log.debug("Authenticating with Spotify")
        redirect(url: "$grailsApplication.config.com.songspot.spotify.authUrl?" +
                "response_type=code&" +
                "scope=playlist-modify-private&" +
                "client_id=$grailsApplication.config.com.songspot.spotify.clientId&" +
                "redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fspotify%2Fcallback&" +
                "state=NA&" +
                "show_dialog=false")
    }

    def callback() {
        SongSpotUserConfig songSpotUserConfig = utilitiesService.getUserConfig()
        songSpotUserConfig.spotifyAuthorizationCode = params.code
        songSpotUserConfig.save()
        redirect(action:"getToken")
    }

    def getToken() {
        spotifyService.getToken()
        redirect(action:"createPlaylist", params:[name:"SongSpot Playlist"])
    }

    def createPlaylist() {
        render spotifyService.createPlaylist(params.name)
    }

    def findArtistByName() {
        render spotifyService.findArtistByName(params.name)
    }

    def getArtistsTopTracks() {
        String artistId = spotifyService.findArtistByName(params.artistName).artists.items.id[0]
        render spotifyService.getArtistsTopTracks(artistId)
    }

    def addTrackToPlaylist() {
        render spotifyService.addTrackToPlaylist(params.topTracksUris, params.playlistId)
    }
}
