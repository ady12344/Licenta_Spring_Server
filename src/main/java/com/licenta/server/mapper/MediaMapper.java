package com.licenta.server.mapper;

import com.licenta.server.TMDBStuff.*;
import com.licenta.server.dto.*;
import com.licenta.server.models.Media;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MediaMapper {

    public static String buildTmdbImageUrl(String path, String size) {
        if (path == null || path.isEmpty()) {
            // Poți returna un URL de placeholder sau null,
            // depinde cum gestionezi în Front-End
            return null;
        }
        // Dimensiuni standard: "w200", "w500", "original"
        return "https://image.tmdb.org/t/p/" + size + path;
    }
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
                .posterPath(buildTmdbImageUrl(dto.getPosterPath(),"w500"))
                .status(dto.getStatus())
                .build();
    }
    public static MovieCardDto mapToMovieCard(TmdbMovieCardDto movieCardDto){
        return MovieCardDto.builder()
                .title(movieCardDto.getTitle())
                .posterPath(buildTmdbImageUrl(movieCardDto.getPosterPath(), "w500"))
                .tmdbId(movieCardDto.getTmdbId()).build();
    }



   /* public static MovieDto newMapToMovieDto(TmdbMovieDto apiResponse) {
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
    }*/
   public static MovieDto newMapToMovieDto(TmdbMovieDto apiResponse) {
       // 1. Verificare de siguranță pentru obiectul principal
       if (apiResponse == null) {
           return MovieDto.builder().build();
       }

       // 2. Extragere Credits cu tipuri generice explicite pentru a evita eroarea "Object"
       // Această linie îi spune Javei exact ce tipuri de date sunt în liste
       CreditsDTO<CastDTO, CrewDTO> credits = apiResponse.getCredits();

       // 3. Procesare Cast (Actori)
       List<CastDTO> topCast = (credits != null && credits.getCast() != null)
               ? credits.getCast().stream()
               .limit(10)
               .map(cast -> {
                   // Actualizăm URL-ul imaginii folosind metoda statică
                   cast.setProfilePath(buildTmdbImageUrl(cast.getProfilePath(), "w200"));
                   return cast;
               })
               .collect(Collectors.toList())
               : List.of();

       // 4. Extragere Regizor (Director)
       String directorName = (credits != null && credits.getCrew() != null)
               ? credits.getCrew().stream()
               // Java știe acum că elementul este CrewDTO, deci găsește getJob()
               .filter(crew -> "Director".equals(crew.getJob()))
               .map(BaseCrewDTO::getName)
               .findFirst()
               .orElse("Unknown Director")
               : "Unknown Director";

       // 5. Extragere Genuri
       List<String> genreNames = (apiResponse.getGenres() != null)
               ? apiResponse.getGenres().stream()
               .map(TmdbGenreDto::getName)
               .toList()
               : List.of();

       // 6. Construcția obiectului final MovieDto
       return MovieDto.builder()
               .tmdbId(apiResponse.getId())
               .title(apiResponse.getTitle())
               .overview(apiResponse.getOverview())
               // Utilizăm metoda statică buildTmdbImageUrl pentru restul imaginilor
               .posterPath(buildTmdbImageUrl(apiResponse.getPosterPath(), "w500"))
               .backdropPath(buildTmdbImageUrl(apiResponse.getBackdropPath(), "original"))
               .releaseDate(apiResponse.getReleaseDate())
               .tmdbRating(apiResponse.getVoteAverage())
               .status(apiResponse.getStatus())
               .directorName(directorName)
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
                .tmdbRating(media.getVoteAverage())
                .genres(media.getGenres())
                .backdropPath(media.getBackdropPath())
                .posterPath(media.getPosterPath())
                .overview(media.getOverview())
                .build();
    }

   /* public static TvDto mapTmdbToTvDto(TmdbTvDto dto){
        // 1. Procesăm lista de cast pentru a curăța rolurile
        List<TvCastDTO> topCast = dto.getCredits().getCast().stream()
                .map(castMember -> {
                    // Filtrăm lista de roluri a fiecărui actor
                    if (castMember.getRoles() != null) {
                        List<TmdbRoleDTO> cleanedRoles = castMember.getRoles().stream()
                                .filter(role -> role.getCharacter() != null && !role.getCharacter().trim().isEmpty())
                                .toList();
                        castMember.setRoles(cleanedRoles);
                    }
                    return castMember;
                })
                // 2. Opțional: Eliminăm actorii care după filtrare nu mai au niciun rol listat
                .filter(castMember -> castMember.getRoles() != null && !castMember.getRoles().isEmpty())
                .limit(10) // Păstrăm primii 10 după curățare
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
    }*/
   public static TvDto mapTmdbToTvDto(TmdbTvDto apiResponse) {
       // 1. Verificare de siguranță
       if (apiResponse == null) {
           return TvDto.builder().build();
       }

       // 2. Extragere Credits (folosind tipurile TV specifice)
       CreditsDTO<TvCastDTO, TvCrewDTO> credits = apiResponse.getCredits();

       // 3. Procesare Cast (Actori) - limitat la 10 și formatare URL
       List<TvCastDTO> topCast = (credits != null && credits.getCast() != null)
               ? credits.getCast().stream()
               .limit(10)
               .map(cast -> {
                   // Actualizăm calea imaginii folosind metoda existentă
                   cast.setProfilePath(buildTmdbImageUrl(cast.getProfilePath(), "w200"));
                   return cast;
               })
               .collect(Collectors.toList())
               : List.of();

       // 4. Extragere Regizor (Director) - logică specifică listei de job-uri TV
       String directorName = (credits != null && credits.getCrew() != null)
               ? credits.getCrew().stream()
               .filter(crew -> crew.getJobs() != null && crew.getJobs().stream()
                       .anyMatch(job -> "Director".equals(job.getJob())))
               .map(BaseCrewDTO::getName)
               .findFirst()
               .orElse("Unknown Director")
               : "Unknown Director";

       // 5. Extragere Genuri
       List<String> genreNames = (apiResponse.getGenres() != null)
               ? apiResponse.getGenres().stream()
               .map(TmdbGenreDto::getName)
               .toList()
               : List.of();

       // 6. Construcția obiectului final TvDto similar cu MovieDto
       return TvDto.builder()
               .tmdbId(apiResponse.getId())
               .title(apiResponse.getName())
               .overview(apiResponse.getOverview())
               .posterPath(buildTmdbImageUrl(apiResponse.getPosterPath(), "w500"))
               .backdropPath(buildTmdbImageUrl(apiResponse.getBackdropPath(), "original"))
               .firstAirDate(apiResponse.getFirstAirDate())
               .tmdbRating(apiResponse.getVoteAverage())
               .status(apiResponse.getStatus())
               .numberOfSeasons(apiResponse.getNumberOfSeasons())
               .directorName(directorName)
               .topCast(topCast)
               .genres(genreNames)
               .build();
   }
    public static TvCardDto mapTmdbToTvCard(TmdbTvCardDto dto){
        return TvCardDto.builder()
                .tmdbTvId(dto.getTmdbTvId())
                .title(dto.getName())
                .posterPath(buildTmdbImageUrl(dto.getPosterPath(), "w500"))
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
