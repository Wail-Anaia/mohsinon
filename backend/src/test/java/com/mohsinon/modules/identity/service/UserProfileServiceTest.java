package com.mohsinon.modules.identity.service;

import com.mohsinon.core.domain.GeoLocation;
import com.mohsinon.core.exception.ResourceNotFoundException;
import com.mohsinon.modules.identity.domain.User;
import com.mohsinon.modules.identity.domain.UserProfile;
import com.mohsinon.modules.identity.domain.UserStatus;
import com.mohsinon.modules.identity.repository.UserProfileRepository;
import com.mohsinon.modules.identity.repository.UserRepository;
import com.mohsinon.modules.identity.web.dto.PrivateUserProfileResponse;
import com.mohsinon.modules.identity.web.dto.PublicUserProfileResponse;
import com.mohsinon.modules.identity.web.dto.UpdateUserProfileRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private DefaultUserProfileService userProfileService;

    private User user;
    private UserProfile userProfile;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User("tariq_m", "tariq@example.com", "secretHash", "Tariq", "Mansour", "Tariq M.");
        user.setId(userId);
        user.setStatus(UserStatus.ACTIVE);

        GeoLocation exactLocation = GeoLocation.of(48.856614, 2.3522219, "Paris", "FR");
        userProfile = new UserProfile(userId, "Bénévole actif.", "https://cdn.mohsinon.org/a1.png", "+33612345678", "fr", exactLocation);
    }

    @Test
    @DisplayName("1. Should get or create default profile when profile does not exist yet")
    void shouldGetOrCreateDefaultProfile() {
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(userProfileRepository.save(any(UserProfile.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        UserProfile profile = userProfileService.getOrCreateProfile(userId);

        assertThat(profile).isNotNull();
        assertThat(profile.getUserId()).isEqualTo(userId);
        assertThat(profile.getPreferredLanguage()).isEqualTo("fr");
        verify(userProfileRepository).save(any(UserProfile.class));
    }

    @Test
    @DisplayName("2. Should retrieve private profile for authenticated user containing personal details")
    void shouldRetrievePrivateProfile() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(userProfile));

        PrivateUserProfileResponse response = userProfileService.getMyProfile(userId);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(userId);
        assertThat(response.getUsername()).isEqualTo("tariq_m");
        assertThat(response.getEmail()).isEqualTo("tariq@example.com");
        assertThat(response.getFirstName()).isEqualTo("Tariq");
        assertThat(response.getLastName()).isEqualTo("Mansour");
        assertThat(response.getDisplayName()).isEqualTo("Tariq M.");
        assertThat(response.getPhoneNumber()).isEqualTo("+33612345678");
        assertThat(response.getBio()).isEqualTo("Bénévole actif.");
        assertThat(response.getAvatarUrl()).isEqualTo("https://cdn.mohsinon.org/a1.png");
        assertThat(response.getCity()).isEqualTo("Paris");
        assertThat(response.getCountryCode()).isEqualTo("FR");
        assertThat(response.getLocation()).isNotNull();
    }

    @Test
    @DisplayName("3. Should retrieve public profile by username for active user")
    void shouldRetrievePublicProfileByUsername() {
        when(userRepository.findByUsername("tariq_m")).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(userProfile));

        PublicUserProfileResponse response = userProfileService.getPublicProfileByUsername("tariq_m");

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(userId);
        assertThat(response.getUsername()).isEqualTo("tariq_m");
        assertThat(response.getDisplayName()).isEqualTo("Tariq M.");
        assertThat(response.getBio()).isEqualTo("Bénévole actif.");
        assertThat(response.getAvatarUrl()).isEqualTo("https://cdn.mohsinon.org/a1.png");
        assertThat(response.getPreferredLanguage()).isEqualTo("fr");
        assertThat(response.getCity()).isEqualTo("Paris");
        assertThat(response.getCountryCode()).isEqualTo("FR");
    }

    @Test
    @DisplayName("4. PRIVACY GUARANTEE: Public profile must NEVER contain phone number or email")
    void publicProfileMustNotContainPrivateContacts() {
        when(userRepository.findByUsername("tariq_m")).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(userProfile));

        PublicUserProfileResponse response = userProfileService.getPublicProfileByUsername("tariq_m");

        // Compile-time and runtime check: PublicUserProfileResponse does not have getPhoneNumber() or getEmail() methods
        assertThat(response.getClass().getDeclaredMethods())
                .noneMatch(m -> m.getName().equalsIgnoreCase("getPhoneNumber") || m.getName().equalsIgnoreCase("getEmail"));
    }

    @Test
    @DisplayName("5. PRIVACY GUARANTEE: Public profile must expose only approximate coordinates, never exact GPS")
    void publicProfileMustUseApproximateLocation() {
        when(userRepository.findByUsername("tariq_m")).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(userProfile));

        PublicUserProfileResponse response = userProfileService.getPublicProfileByUsername("tariq_m");

        assertThat(response.getApproximateLocation()).isNotNull();
        // Exact was 48.856614, approximate rounded to 2 decimals is 48.86
        assertThat(response.getApproximateLocation().getLatitude()).isEqualTo(48.86);
        // Exact was 2.3522219, approximate rounded to 2 decimals is 2.35
        assertThat(response.getApproximateLocation().getLongitude()).isEqualTo(2.35);
    }

    @Test
    @DisplayName("6. Should update user profile and user names successfully")
    void shouldUpdateUserProfile() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(userProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateUserProfileRequest request = new UpdateUserProfileRequest(
                "Tariq Updated",
                "Mansour Updated",
                "Tariq The Builder",
                "Nouvelle bio.",
                "https://cdn.mohsinon.org/a2.png",
                "+33698765432",
                "ar",
                "Lyon",
                "FR",
                45.764043,
                4.835659
        );

        PrivateUserProfileResponse response = userProfileService.updateMyProfile(userId, request);

        assertThat(response.getFirstName()).isEqualTo("Tariq Updated");
        assertThat(response.getLastName()).isEqualTo("Mansour Updated");
        assertThat(response.getDisplayName()).isEqualTo("Tariq The Builder");
        assertThat(response.getBio()).isEqualTo("Nouvelle bio.");
        assertThat(response.getAvatarUrl()).isEqualTo("https://cdn.mohsinon.org/a2.png");
        assertThat(response.getPhoneNumber()).isEqualTo("+33698765432");
        assertThat(response.getPreferredLanguage()).isEqualTo("ar");
        assertThat(response.getCity()).isEqualTo("Lyon");

        verify(userRepository).save(user);
        verify(userProfileRepository).save(userProfile);
    }

    @Test
    @DisplayName("7. IMMUTABILITY: Protected fields (id, username, email, status) must remain unchanged during profile update")
    void protectedFieldsRemainUntouched() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(userProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateUserProfileRequest request = new UpdateUserProfileRequest();
        request.setBio("Updated bio only.");

        PrivateUserProfileResponse response = userProfileService.updateMyProfile(userId, request);

        assertThat(response.getId()).isEqualTo(userId);
        assertThat(response.getUsername()).isEqualTo("tariq_m");
        assertThat(response.getEmail()).isEqualTo("tariq@example.com");
        assertThat(response.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("8. Should reject invalid parameters on service operations")
    void rejectNullParameters() {
        assertThatThrownBy(() -> userProfileService.getMyProfile(null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> userProfileService.getPublicProfileByUsername(null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> userProfileService.getPublicProfileByUsername("   "))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> userProfileService.getPublicProfileById(null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> userProfileService.updateMyProfile(null, new UpdateUserProfileRequest()))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> userProfileService.updateMyProfile(userId, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("9. Should throw ResourceNotFoundException when user is not found")
    void throwWhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userProfileService.getMyProfile(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User");

        when(userRepository.findByUsername("unknown_user")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userProfileService.getPublicProfileByUsername("unknown_user"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User");
    }

    @Test
    @DisplayName("10. Should throw ResourceNotFoundException for public profile of suspended/inactive user")
    void throwWhenPublicUserIsInactiveOrSuspended() {
        user.setStatus(UserStatus.SUSPENDED);
        when(userRepository.findByUsername("tariq_m")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userProfileService.getPublicProfileByUsername("tariq_m"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User");
    }
}
