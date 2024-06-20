package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.Repository.UserRepo;
import com.example.minionlinemarket.Services.MyUserService;
import com.example.minionlinemarket.Model.MyUser;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
//import com.sendgrid.*;
//import java.io.IOException;

import java.util.List;

@Service
public class UserServiceImpl implements MyUserService {
    private UserRepo userRepository;

    public UserServiceImpl(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public MyUser save(MyUser user) {
        return userRepository.save(user);
    }


    @Override
    public MyUser findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<MyUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public MyUser loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not Found")
                );

    }

//    public String verify(String token) throws IOException {
//        Email from = new Email("test@example.com");
//        String subject = "Sending with SendGrid is Fun";
//        Email to = new Email("test@example.com");
//        Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
//        Mail mail = new Mail(from, subject, to, content);
//
//        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
//        Request request = new Request();
//        try {
//            request.setMethod(Method.POST);
//            request.setEndpoint("mail/send");
//            request.setBody(mail.build());
//            Response response = sg.api(request);
//            System.out.println(response.getStatusCode());
//            System.out.println(response.getBody());
//            System.out.println(response.getHeaders());
//        } catch (IOException ex) {
//            throw ex;
//        }
//        return subject;
//    }

}
