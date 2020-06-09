package ml.socshared.worker.security.token;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.worker.security.jwt.JwtTokenProvider;
import ml.socshared.worker.security.model.TokenObject;
import ml.socshared.worker.security.request.ServiceTokenRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@Aspect
public class TokenGetter {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenGetter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        init();
    }

    private TokenObject tokenFB;
    private TokenObject tokenVK;
    private TokenObject tokenBSTAT;
    private TokenObject tokenStorageService;

    private void init() {
        tokenFB = new TokenObject();
        tokenVK = new TokenObject();
        tokenBSTAT = new TokenObject();
        tokenStorageService = new TokenObject();
    }

    @Before("execution(* ml.socshared.worker.service.impl.FacebookServiceImpl.*(..))")
    public TokenObject initTokenFB() {
        if (tokenFB.getToken() != null && jwtTokenProvider.validateServiceToken(tokenFB.getToken())) {
            return tokenFB;
        }

        ServiceTokenRequest request = new ServiceTokenRequest();
        request.setFromServiceId(UUID.fromString("25086e71-269b-46ff-aa48-23f7ffba3bf9"));
        request.setToServiceId(UUID.fromString("f7e14d85-415c-4ab9-b285-a6481d79f507"));
        request.setToSecretService(UUID.fromString("427d82bb-b367-40b4-bee8-b18e32480899"));

        this.tokenFB.setToken(jwtTokenProvider.buildServiceToken(request).getToken());

        return tokenFB;
    }
    @Before("execution(* ml.socshared.worker.service.impl.VkServiceImpl.*(..))")
    public TokenObject initTokenVK() {
        if (tokenVK != null && jwtTokenProvider.validateServiceToken(tokenVK.getToken())) {
            return tokenVK;
        }

        ServiceTokenRequest request = new ServiceTokenRequest();
        request.setFromServiceId(UUID.fromString("25086e71-269b-46ff-aa48-23f7ffba3bf9"));
        request.setToServiceId(UUID.fromString("cb43eee3-3468-4cc2-b6ed-63419e8726ce"));
        request.setToSecretService(UUID.fromString("f769cb1c-bf08-478d-8218-0bb347369dd7"));

        this.tokenVK.setToken(jwtTokenProvider.buildServiceToken(request).getToken());

        return tokenVK;
    }


    @Before("execution(* ml.socshared.worker.service.impl.BStatServiceImpl.*(..))")
    public TokenObject initTokenBSTAT() {
        if (tokenBSTAT != null && jwtTokenProvider.validateServiceToken(tokenBSTAT.getToken())) {
            return tokenBSTAT;
        }

        ServiceTokenRequest request = new ServiceTokenRequest();
        request.setFromServiceId(UUID.fromString("25086e71-269b-46ff-aa48-23f7ffba3bf9"));
        request.setToServiceId(UUID.fromString("e7ee788d-c59e-4a96-bdaf-52d6b33df1f3"));
        request.setToSecretService(UUID.fromString("b8500899-b1a1-4b99-984f-08aed46d1aea"));

        this.tokenBSTAT.setToken(jwtTokenProvider.buildServiceToken(request).getToken());

        return tokenBSTAT;
    }

    @Before("execution(* ml.socshared.worker.service.impl.StorageServiceImpl.*(..))")
    public TokenObject initTokenStorageService() {
        if (tokenStorageService != null && jwtTokenProvider.validateServiceToken(tokenStorageService.getToken())) {
            return tokenStorageService;
        }

        ServiceTokenRequest request = new ServiceTokenRequest();
        request.setFromServiceId(UUID.fromString("25086e71-269b-46ff-aa48-23f7ffba3bf9"));
        request.setToServiceId(UUID.fromString("64141ce5-5604-4ade-ada2-e38cf7d2522c"));
        request.setToSecretService(UUID.fromString("5b21977e-166f-471b-a7a7-c60b20e18cf9"));

        this.tokenStorageService.setToken(jwtTokenProvider.buildServiceToken(request).getToken());

        return tokenStorageService;
    }

    public TokenObject getTokenFB() {
        return tokenFB;
    }

    public TokenObject getTokenVK() {
        return tokenVK;
    }

    public TokenObject getTokenBSTAT() {
        return tokenBSTAT;
    }

    public TokenObject getTokenStorageService() {
        return tokenStorageService;
    }
}
