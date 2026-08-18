package com.marotech.skillhub.action.register;

import com.marotech.skillhub.action.HubActionBeanContext;
import com.marotech.skillhub.components.config.Config;
import com.marotech.skillhub.components.service.RepositoryService;
import com.marotech.skillhub.model.*;
import com.marotech.skillhub.repository.GenericRepository;
import com.marotech.skillhub.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.ValidationErrors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Locale;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RegisterActionBeanPart2Test {

    @InjectMocks
    private RegisterActionBeanPart2 actionBean;

    @Mock
    private RepositoryService repositoryService;

    @Mock
    private HubActionBeanContext context;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private GenericRepository repository;

    @Mock
    private Config config; // type from UserBaseActionBean - adjust if different

    private ValidationErrors validationErrors;
    private Properties configProperties;

    @BeforeEach
    void setUp() {
        validationErrors = new ValidationErrors();

        // Wire mocks into BaseActionBean fields via reflection
        ReflectionTestUtils.setField(actionBean, "repositoryService", repositoryService);
        ReflectionTestUtils.setField(actionBean, "config", config);

        actionBean.setContext(context);
        lenient().when(context.getRequest()).thenReturn(request);
         lenient().when(request.getSession()).thenReturn(session);
        lenient().when(context.getValidationErrors()).thenReturn(validationErrors);
        lenient().when(repositoryService.getRepository()).thenReturn(repository);

        configProperties = new Properties();
        configProperties.setProperty("country", "Zimbabwe");
        configProperties.setProperty("system.available.countries", "Zimbabwe,South Africa,Botswana");
        lenient().when(config.getProperty("country")).thenReturn("Zimbabwe");
        lenient().when(config.getProperty("system.available.countries")).thenReturn("Zimbabwe,South Africa,Botswana");

        // Default form data
        actionBean.setFirstName("John");
        actionBean.setMiddleName("M");
        actionBean.setLastName("Doe");
        actionBean.setNationalId("63-123456A12");
        actionBean.setPassword("Password123");
        actionBean.setAddress("123 Main St");
        actionBean.setTown("Harare");
        actionBean.setSuburb("Avondale");
        actionBean.setMobilephone("077 123 4567");
        actionBean.setRegType(RegType.TALENT);
    }

    @Test
    void view_shouldClearSessionAndForwardToJsp() {
        Resolution resolution = actionBean.view();

        //verify(session).setAttribute(Constants.LOGGED_IN_USER, null);
        assertTrue(resolution instanceof ForwardResolution);
        ForwardResolution forward = (ForwardResolution) resolution;
        assertEquals("/WEB-INF/jsp/user/register/register2.jsp", forward.getPath());
    }

    @Test
    void save_shouldFailWhenUserExistsByNationalId() throws Exception {
        when(repositoryService.findUserByNationalId("63-123456A12")).thenReturn(new User());

        Resolution resolution = actionBean.save();

        assertTrue(resolution instanceof ForwardResolution);
        assertFalse(validationErrors.isEmpty());
        assertNotNull(validationErrors.get("username"));
        verify(repositoryService, never()).save(any(AuthUser.class));
    }

    @Test
    void save_shouldCreateNewCityAndSuburbWhenNotExist() throws Exception {
        when(repositoryService.findUserByNationalId(any())).thenReturn(null);
        when(repositoryService.fetchCityByName("Harare", "Zimbabwe")).thenReturn(null);
        when(repositoryService.fetchSuburbByName(any(), eq("Avondale"))).thenReturn(null);
        UserRole userRole = new UserRole();
        userRole.setRoleName(Constants.USER);
        UserRole talentRole = new UserRole();
        talentRole.setRoleName(Constants.TALENT);
        when(repositoryService.findUserRoleByRoleName(Constants.USER)).thenReturn(userRole);
        when(repositoryService.findUserRoleByRoleName(Constants.TALENT)).thenReturn(talentRole);

        Resolution resolution = actionBean.save();

        // Verify city created
        ArgumentCaptor<City> cityCaptor = ArgumentCaptor.forClass(City.class);
        verify(repositoryService).save(cityCaptor.capture());
        // Verify suburb, address, authUser, user saved - total 5 saves before final
        verify(repositoryService, atLeast(5)).save(any());

        assertTrue(resolution instanceof RedirectResolution);
        //assertEquals("/web/inbox/list", ((RedirectResolution)
          //      resolution).getUrl(Locale.getDefault()));
        //verify(session).setAttribute(eq(Constants.LOGGED_IN_USER), any(User.class));
    }

    @Test
    void save_shouldReuseExistingCityAndSuburb() throws Exception {
        when(repositoryService.findUserByNationalId(any())).thenReturn(null);
        City existingCity = new City();
        existingCity.setName("Harare");
        Suburb existingSuburb = new Suburb();
        existingSuburb.setCity(existingCity);
        existingSuburb.setName("Avondale");
        when(repositoryService.fetchCityByName("Harare", "Zimbabwe")).thenReturn(existingCity);
        when(repositoryService.fetchSuburbByName(existingCity, "Avondale")).thenReturn(existingSuburb);

        UserRole userRole = mock(UserRole.class);
        when(repositoryService.findUserRoleByRoleName(Constants.USER)).thenReturn(userRole);

        actionBean.setRegType(RegType.INTERNAL_USER); // non-talent
        Resolution resolution = actionBean.save();

        // Should NOT save new city/suburb
        verify(repositoryService, never()).save(any(City.class));
        verify(repositoryService, never()).save(any(Suburb.class));
        assertTrue(resolution instanceof RedirectResolution);
    }

    @Test
    void save_shouldNormalizeMobileAndEncodePassword() throws Exception {
        when(repositoryService.findUserByNationalId(any())).thenReturn(null);
        when(repositoryService.fetchCityByName(any(), any())).thenReturn(new City());
        when(repositoryService.fetchSuburbByName(any(), any())).thenReturn(new Suburb());
        when(repositoryService.findUserRoleByRoleName(any())).thenReturn(new UserRole());

        actionBean.setMobilephone(" 077 123 4567 ");
        actionBean.save();

        ArgumentCaptor<AuthUser> authCaptor = ArgumentCaptor.forClass(AuthUser.class);
        verify(repositoryService).save(authCaptor.capture());
        AuthUser savedAuth = authCaptor.getAllValues().get(0);
        assertEquals("0771234567", savedAuth.getUserName()); // spaces removed + lowercased
        assertNotEquals("Password123", savedAuth.getPassword()); // encoded
        assertTrue(savedAuth.getPassword().length() > 10);
    }

    @Test
    void save_shouldAddOnlyUserRoleWhenNotTalent() throws Exception {
        when(repositoryService.findUserByNationalId(any())).thenReturn(null);
        when(repositoryService.fetchCityByName(any(), any())).thenReturn(new City());
        when(repositoryService.fetchSuburbByName(any(), any())).thenReturn(new Suburb());
        UserRole userRole = new UserRole();
        userRole.setRoleName(Constants.USER);
        when(repositoryService.findUserRoleByRoleName(Constants.USER)).thenReturn(userRole);

        actionBean.setRegType(RegType.INTERNAL_USER);
        actionBean.save();

        verify(repositoryService, never()).findUserRoleByRoleName(Constants.TALENT);
    }

    @Test
    void save_shouldAddTalentRoleWhenRegTypeIsTalent() throws Exception {
        when(repositoryService.findUserByNationalId(any())).thenReturn(null);
        when(repositoryService.fetchCityByName(any(), any())).thenReturn(new City());
        when(repositoryService.fetchSuburbByName(any(), any())).thenReturn(new Suburb());
        UserRole userRole = new UserRole();
        UserRole talentRole = new UserRole();
        when(repositoryService.findUserRoleByRoleName(Constants.USER)).thenReturn(userRole);
        when(repositoryService.findUserRoleByRoleName(Constants.TALENT)).thenReturn(talentRole);

        actionBean.setRegType(RegType.TALENT);
        actionBean.save();

        verify(repositoryService).findUserRoleByRoleName(Constants.TALENT);
    }

    //@Test
    void save_shouldRollbackAndReturnErrorWhenUserSaveFails() throws Exception {
        when(repositoryService.findUserByNationalId(any())).thenReturn(null);
        when(repositoryService.fetchCityByName(any(), any())).thenReturn(new City());
        when(repositoryService.fetchSuburbByName(any(), any())).thenReturn(new Suburb());
        // First save(authUser) succeeds, second save(user)
        // with address succeeds, third save(user) throws
        doThrow(new RuntimeException("DB error"))
                .when(repositoryService).save(any(User.class));

        Resolution resolution = actionBean.save();

        assertTrue(resolution instanceof ForwardResolution);
        verify(repository).delete(any(User.class));
        verify(repository).delete(any(AuthUser.class));
        assertFalse(validationErrors.isEmpty());
        assertNotNull(validationErrors.get("username"));
    }

    @Test
    void handleValidationErrors_shouldForwardToErrorPage() throws Exception {
        ValidationErrors errors = new ValidationErrors();
        Resolution res = actionBean.handleValidationErrors(errors);
        assertTrue(res instanceof ForwardResolution);
    }

    @Test
    void getters_shouldReturnEnumsAndCountries() {
        assertArrayEquals(Gender.values(), actionBean.getGenders());
        assertArrayEquals(new String[]{"Zimbabwe", "South Africa", "Botswana"}, actionBean.getCountries());
        assertEquals("register", actionBean.getNavSection());
    }
}