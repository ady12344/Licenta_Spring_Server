package com.licenta.server.mapper;

import com.licenta.server.TMDBStuff.*;
import com.licenta.server.dto.*;
import com.licenta.server.models.Media;
import java.util.List;
import java.util.stream.Collectors;

public class MediaMapper {
    //Movies
    public static MovieDto mapMediaToMovieDto(Media media){
        return MovieDto.builder()
                .tmdbId(media.getTmdbId())
                .title(media.getTitle())
                .backdropPath(media.getBackdropPath())
                .genres(media.getGenres())
                .overview(media.getOverview())
                .tmdbRating(media.getVoteAverage())
                .releaseDate(media.getReleaseDate() == null ? null : media.getReleaseDate().toString())
                .posterPath(media.getPosterPath())
                .status(media.getStatus())
                .build();
    }
    public static MovieDto mapTmdbToMovieDto(TmdbMovieDto dto){
        return MovieDto.builder()
                .tmdbId(dto.getId())
                .title(dto.getTitle())
                .backdropPath(dto.getBackdropPath())
                .genres(dto.getGenres() == null ? List.of() : dto.getGenres().stream().map(TmdbGenreDto::getName).toList())
                .overview(dto.getOverview())
                .tmdbRating(dto.getVoteAverage())
                .releaseDate(dto.getReleaseDate())
                .posterPath(dto.getPosterPath())
                .status(dto.getStatus())
                .build();
    }
    public static MovieCardDto mapToMovieCard(TmdbMovieCardDto movieCardDto){
        return MovieCardDto.builder()
                .title(movieCardDto.getTitle())
                .posterPath(movieCardDto.getPosterPath())
                .tmdbId(movieCardDto.getTmdbId()).build();
    }



    public static MovieDto newMapToMovieDto(TmdbMovieDto apiResponse) {
        // 2. Extragere Top Cast (primii 10)
        List<CastDTO> topCast = apiResponse.getCredits().getCast().stream()
                .limit(10)
                .collect(Collectors.toList());

        // 3. Extragere Genuri (numele lor sub formă de String)
        List<String> genreNames = apiResponse.getGenres().stream()
                .map(TmdbGenreDto::getName)
                .collect(Collectors.toList());

        // 4. Construcția obiectului final
        return MovieDto.builder()
                .tmdbId(apiResponse.getId())
                .title(apiResponse.getTitle())
                .overview(apiResponse.getOverview())
                .posterPath(apiResponse.getPosterPath())
                .backdropPath(apiResponse.getBackdropPath())
                .releaseDate(apiResponse.getReleaseDate())
                .tmdbRating(apiResponse.getVoteAverage())
                .status(apiResponse.getStatus())
                .directorName(apiResponse.getCredits().getCrew().stream()
                        .filter(crew -> "Director".equals(crew.getJob()))
                        .map(CrewDTO::getName) // Extragem doar String-ul cu numele
                        .findFirst()           // Îl luăm pe primul
                        .orElse("Unknown"))    // Fallback dacă nu există regizor în listă
                .topCast(topCast)
                .genres(genreNames)
                .build();
    }


    //Tv Shows
    public static TvDto mapMediaToTvDto(Media media){
        return TvDto.builder()
                .tmdbId(media.getTmdbId())
                .title(media.getTitle())
                .firstAirDate(media.getFirstAirDate() == null ? null : media.getFirstAirDate().toString())
                .status(media.getStatus())
                .numberOfSeasons(media.getNumberOfSeasons())
                .voteAverage(media.getVoteAverage())
                .genres(media.getGenres())
                .backdropPath(media.getBackdropPath())
                .posterPath(media.getPosterPath())
                .overview(media.getOverview())
                .build();
    }

    public static TvDto mapTmdbToTvDto(TmdbTvDto dto){
        List<TvCastDTO> topCast = dto.getCredits().getCast().stream()
                .limit(10)
                .toList();

        return TvDto.builder()
                .tmdbId(dto.getId())
                .title(dto.getName())
                .voteAverage(dto.getVoteAverage())
                .overview(dto.getOverview())
                .status(dto.getStatus())
                .firstAirDate(dto.getFirstAirDate())
                .numberOfSeasons(dto.getNumberOfSeasons())
                .genres(dto.getGenres().stream().map(TmdbGenreDto::getName).toList())
                .backdropPath(dto.getBackdropPath())
                .directorName(dto.getCredits().getCrew().stream()
                        .filter(crew -> crew.getJobs().stream()
                                .anyMatch(job -> "Director".equals(job.getJob()))) // Verificăm dacă are job-ul de "Director"
                        .findFirst() // Îl luăm pe primul găsit
                        .map(TvCrewDTO::getName) // Extragem doar numele
                        .orElse("Unknown Director")) // Fallback dacă nu găsim nimic
                .topCast(topCast)
                .posterPath(dto.getPosterPath())
                .build();
    }
    public static TvCardDto mapTmdbToTvCard(TmdbTvCardDto dto){
        return TvCardDto.builder()
                .tmdbTvId(dto.getTmdbTvId())
                .title(dto.getName())
                .posterPath(dto.getPosterPath())
                .build();
    }

    public static SeasonCardDto mapTmdbSeasonCardToSeasonCardDto(TmdbSeasonCardDto dto){
        return SeasonCardDto.builder()
                .name(dto.getName())
                .seasonNumber(dto.getSeasonNumber())
                .posterPath(dto.getPosterPath())
                .build();
    }

}
