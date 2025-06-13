package projeto_integrado.controllers.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import projeto_integrado.Infra.EmailService;
import projeto_integrado.controllers.Entidades.User;
import projeto_integrado.controllers.Entidades.Repositories.RepositorioUser;

	@Controller
	@RequestMapping("/login")
	public class login{
		@GetMapping
			public String mostrarlogin() {
				return "Login";
			}

		@Autowired
		private RepositorioUser  userRepository;
		@Autowired
		private EmailService emailService;

		@PostMapping
		public String verificaLogin(@RequestParam String email, @RequestParam String senha, HttpSession session, Model model) {
			User usuario = userRepository.findByEmail(email);

			if (usuario != null && usuario.getSenha().equals(senha)) {
				session.setAttribute("usuariologado", usuario);
				session.setMaxInactiveInterval(900);
				return "redirect:/logado";
			} else {
				return "redirect:/login";
			}
		}


		@GetMapping("/recuperar-senha")
		public String mostrarFormularioRecuperarSenha() {
		    return "recuperar-senha"; 
		}

		@PostMapping("/recuperar-senha")
		public String recuperarSenha(@RequestParam String email, Model model) {
			User usuario = userRepository.findByEmail(email);

			if (usuario != null) {
				String assunto = "Recuperação de Senha";
				String corpo = "Olá, " + usuario.getNome() + "\n\nSua senha é: " + usuario.getSenha();
				emailService.enviarEmail(email, assunto, corpo);
				model.addAttribute("mensagem", "Senha enviada para o e-mail.");
			} else {
				model.addAttribute("mensagem", "E-mail não encontrado.");
			}

			return "recuperar-Senha";
		}



	}
