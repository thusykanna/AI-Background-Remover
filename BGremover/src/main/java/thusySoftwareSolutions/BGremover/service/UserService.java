package thusySoftwareSolutions.BGremover.service;

import thusySoftwareSolutions.BGremover.dto.UserDTO;

public interface UserService {

    UserDTO saveUser(UserDTO userDTO);

    UserDTO getUserByClerkId(String clerkId);

    UserDTO addCredits(String clerkId, int creditsToAdd);
}
