package com.example.minionlinemarket.Services.Imp;

import com.example.minionlinemarket.Model.Dto.Request.AddressDto;
import com.example.minionlinemarket.Model.Dto.Response.AddressDetailDto;
import com.example.minionlinemarket.Model.Address;
import com.example.minionlinemarket.Model.Buyer;
import com.example.minionlinemarket.Repository.AddressRepository;
import com.example.minionlinemarket.Repository.BuyerRepository;
import com.example.minionlinemarket.Services.AddressService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AddressServiceImp implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    public AddressDetailDto addAddress(Long buyerId, AddressDto addressDto) {
        Buyer buyer = buyerRepository.findById(buyerId).orElseThrow(() -> new RuntimeException("Buyer not found"));
        Address address = new Address();
        address.setStreet(addressDto.getStreet());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setPostalCode(addressDto.getPostalCode());
        address.setCountry(addressDto.getCountry());
        address.setBuyer(buyer);
        Address savedAddress = addressRepository.save(address);
        buyer.getAddresses().add(address);
        buyerRepository.save(buyer);
        return mapToDetailDto(savedAddress);
    }

    public Set<AddressDetailDto> getAddressesByBuyerId(Long buyerId) {
        Buyer buyer = buyerRepository.findById(buyerId).orElseThrow(() -> new RuntimeException("Buyer not found"));
        return buyer.getAddresses().stream().map(this::mapToDetailDto).collect(Collectors.toSet());
    }

    private AddressDetailDto mapToDetailDto(Address address) {
        return AddressDetailDto.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .build();
    }
}
