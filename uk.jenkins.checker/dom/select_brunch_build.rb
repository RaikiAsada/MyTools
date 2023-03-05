# frozen_string_literal: true

class SelectBrunchBuild < JenkinsJob
  def initialize(rootDir, jobName)
    super(rootDir, jobName)
  end
  def get_brunch
    return @buildXml.get_parameter(ParameterType::JENKINS_GIT_PARAM, 'BRANCH')
  end
end

def load_select_brunch_build(job)
  Dir.chdir(getBuildsPath)
  return Dir.glob('*')
            .select {|file| File.directory?(file)}
            .select { | dir| dir =~ /^\d+$/ }
            .map {|buildDir| SelectBrunchBuild.new(getJobPath, buildDir)}
end